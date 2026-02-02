package com.test.memoapp.calendar.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.memo.data.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoRepository
) : ViewModel() {

    val yearMonth = YearMonth.from(LocalDate.now())
    val currentMonth =
        savedStateHandle.getStateFlow("currentMonth", YearMonth.now(ZoneId.of("Asia/Seoul")))
    val calendarFirstDay = savedStateHandle.getStateFlow(
        "calendarFirstDay",
        DateFormatUtils.convertLocalTimeToLong(yearMonth.atDay(1))
    )
    val calendarEndDay = savedStateHandle.getStateFlow(
        "calendarLastDay", DateFormatUtils.convertLocalDateTimeToLong(
            yearMonth.atEndOfMonth().atTime(
                LocalTime.MAX
            )
        )
    )
    val selectDay = savedStateHandle.getStateFlow("selectDay", LocalDateTime.now().dayOfMonth)

    val daySelected = savedStateHandle.getStateFlow("daySelected", initialValue = false)


    val monthSchedule = calendarEndDay.flatMapLatest { month ->
        repository.getMemoByTime(calendarFirstDay.value, calendarEndDay.value)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
//    val monthSchedule = repository.getMemoByTime(calendarFirstDay.value, calendarEndDay.value)
//        .stateIn(
//            started = SharingStarted.WhileSubscribed(5000),
//            scope = viewModelScope,
//            initialValue = emptyList()
//        )

    private val selectTime = selectDay.map { day ->
        DateFormatUtils.convertLocalDateTimeToLong(
            LocalDateTime.of(
                currentMonth.value.year,
                currentMonth.value.monthValue,
                selectDay.value,
                0,
                0
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0L // 초기값 설정
    )

//    private val _time = MutableStateFlow(time)


    val daySchedule = selectTime.flatMapLatest { time ->
        repository.getMemoByTime(time, time)
    }.stateIn(
        started = SharingStarted.WhileSubscribed(5000),
        scope = viewModelScope,
        initialValue = emptyList()
    )

    fun handleAction(action: EventAction) {
        when (action) {
            is EventAction.monthChange -> {
                println("action.month = ${action.month}")
                savedStateHandle["currentMonth"] = action.month
                savedStateHandle["daySelected"] = false
                savedStateHandle["calendarFirstDay"] = DateFormatUtils.convertLocalTimeToLong(action.month.atDay(1))
                savedStateHandle["calendarEndDay"] = DateFormatUtils.convertLocalDateTimeToLong(
                    action.month.atEndOfMonth().atTime(
                        LocalTime.MAX
                    )
                )
            }

            is EventAction.selectDay -> {
                savedStateHandle["selectDay"] = action.day.date.dayOfMonth
                savedStateHandle["daySelected"] = true
            }

            is EventAction.setCalendarDays -> {
                savedStateHandle["calendarFirstDay"] = action.firstDay
                savedStateHandle["calendarLastDay"] = action.lastDay
            }
        }
    }
}

