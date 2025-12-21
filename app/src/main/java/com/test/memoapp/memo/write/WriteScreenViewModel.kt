package com.test.memoapp.memo.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateUtils
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.MemoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoRepositoryImpl
) : ViewModel() {
    val title = savedStateHandle.getStateFlow(key = "title", initialValue = "")

    val selectedTime = savedStateHandle.getStateFlow<Long?>("date", null)

    val formattedDate = selectedTime.map { timestamp ->
        timestamp?.let {
            DateUtils.convertLongToString(timestamp, DateConvertType.DEFAULT)
        } ?: "선택된 날짜 없음"
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), "")

    fun onTitleChanged(newTitle: String) {
        savedStateHandle["title"] = newTitle
    }

    fun onDateSelected(newTimestamp: Long?) {
        savedStateHandle["date"] = newTimestamp
    }

    suspend fun saveMemo() {
        println("db call ")
        if (selectedTime.value != null) {
            repository.saveMemo(MemoEntity(title = title.value,
                    scheduleTime = selectedTime.value!!
                )
            )
        } else {
            repository.saveMemo(
                MemoEntity(
                    title = title.value,
                    scheduleTime = 0
                )
            )
        }
        val allMemos = repository.getAllMemos()
        allMemos.collect { list ->
            println("list = ${list}")
        }
        println("allMemos = ${allMemos}")
    }

}