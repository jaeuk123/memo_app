package com.test.memoapp.calendar.screen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.data.MemoEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val currentMonth = viewModel.currentMonth.collectAsStateWithLifecycle()
    val selectDay = viewModel.selectDay.collectAsStateWithLifecycle()
    val monthSchedule = viewModel.monthSchedule.collectAsStateWithLifecycle()
    val daySchedule = viewModel.daySchedule.collectAsStateWithLifecycle()
    val daySelected = viewModel.daySelected.collectAsStateWithLifecycle()

    val calendarState = CalendarState(
        currentMonth.value,
        selectDay.value,
        monthSchedule = monthSchedule.value,
        daySchedule = daySchedule.value,
        daySelected = daySelected.value
    )

    CalendarContent(calendarState, onAction = { action ->
        viewModel.handleAction(action)
    })
}

data class CalendarState(
    val currentMonth: YearMonth,
    val selectDay: Int,
    val monthSchedule: List<MemoEntity>,
    val daySchedule: List<MemoEntity>,
    val daySelected: Boolean
)

sealed class EventAction {
    data class monthChange(val month: YearMonth) : EventAction()
    data class selectDay(val day: CalendarDay) : EventAction()
    data class setCalendarDays(val firstDay: Long, val lastDay: Long) : EventAction()

}

@Composable
fun CalendarContent(calendarState: CalendarState, onAction: (EventAction) -> Unit) {
    val startMonth = remember { calendarState.currentMonth.minusMonths(600) }
    val endMonth = remember { calendarState.currentMonth.plusMonths(600) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val firstVisibleMonth = YearMonth.now(ZoneId.of("Asia/Seoul"))

    val state = rememberCalendarState(
        startMonth = startMonth,
        firstVisibleMonth = firstVisibleMonth,
        endMonth = endMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleMonth }
            .collect { calendarMonth ->
                println(" LaunchedEffect 1")
                onAction(EventAction.monthChange(calendarMonth.yearMonth))
            }
    }
    val lastVisibleMonth = state.lastVisibleMonth
    val firstDay =
        DateFormatUtils.convertLocalTimeToLong(lastVisibleMonth.weekDays.first().first().date)
    val lastDateOnScreen =
        DateFormatUtils.convertLocalTimeToLong(lastVisibleMonth.weekDays.last().last().date)

    onAction(EventAction.setCalendarDays(firstDay, lastDateOnScreen))

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            DaysOfWeekTitle(
                calendarState.currentMonth,
                daysOfWeek = daysOfWeekFromLocale(),
                { month -> EventAction.monthChange(month) })

            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = (day.date.dayOfMonth == calendarState.selectDay
                                && day.date.yearMonth == calendarState.currentMonth),
                        hasSchedule = false,
                        isTodoDone = false
                    ) {
                        onAction(EventAction.selectDay(day))
                    }
                },
            )

            LazyColumn(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (calendarState.daySelected) {
                    itemsIndexed(calendarState.daySchedule) { index, data ->
                        MemoItemIndex(data)
                        println("1 data.title = ${data.title}")
                    }
                } else {
                    itemsIndexed(calendarState.monthSchedule) { index, data ->
                        MemoItemIndex(data)
                        println("2 data.title = ${data.title}")
                    }
                }
            }

            println("calendarState.daySelected = ${calendarState.daySelected}")
            if (calendarState.daySelected && calendarState.daySchedule.isEmpty() ||
                !calendarState.daySelected && calendarState.monthSchedule.isEmpty()
            ) {
                var text: String
                if (calendarState.daySelected) {
                    text = "등록된 일정이 없습니다."
                } else {
                    text = "이번달에 아직 등록된 일정이 없습니다."
                }
                Text(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = text,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun MemoItemIndex(data: MemoEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(57.dp),
                    text = DateFormatUtils.convertLongToString(
                        data.scheduleTime,
                        DateConvertType.Date
                    )
                )
            }
            Text(data.title)

        }
    }
}

@Composable
@Preview
private fun MemoItemIndexPreview() {
    MemoItemIndex(DummyItems.memo)
}

@Preview
@Composable
fun PreviewContent() {
    val todday = LocalDate.now(ZoneId.of("Asia/Seoul"))
    CalendarContent(
        CalendarState(
            currentMonth = YearMonth.now(),
            selectDay = todday.dayOfMonth,
            DummyItems.memoList,
            DummyItems.memoList,
            false
        ), {})

}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    hasSchedule: Boolean = false,  // 일정이 있는가?
    isTodoDone: Boolean = false,    // 오늘 할 일을 다 끝냈는가?
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // 정사각형 모양
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) {
                            Color.Cyan.copy(alpha = 0.3f)
                        } else Color.Transparent, shape = CircleShape
                    )
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = if (day.position == DayPosition.MonthDate) Color.Black else Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            // 일정/Todo 상태 표시 (하단에 작은 점들)
            Row(modifier = Modifier.padding(top = 2.dp)) {
                if (hasSchedule) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Red, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                if (isTodoDone) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Green, CircleShape)
                    )
                }
            }
        }
    }
}

fun daysOfWeekFromLocale(): List<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // 시작 요일에 맞게 순서 재배열
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek.toList()
}

@Composable
fun DaysOfWeekTitle(
    month: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    changeMonth: (YearMonth) -> Unit
) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { changeMonth(month.minusMonths(1)) }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "")
            }
            Text(
                text = "${month}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(onClick = { changeMonth(month.plusMonths(1)) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = ""
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // 상하 여백
        ) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f), // 7등분
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN), // "월", "화" 등
                    style = MaterialTheme.typography.labelMedium,
                    color = when (dayOfWeek) {
                        DayOfWeek.SUNDAY -> Color.Red    // 일요일은 빨간색
                        DayOfWeek.SATURDAY -> Color.Blue // 토요일은 파란색
                        else -> Color.Gray               // 평일은 회색
                    }
                )
            }
        }
    }
}