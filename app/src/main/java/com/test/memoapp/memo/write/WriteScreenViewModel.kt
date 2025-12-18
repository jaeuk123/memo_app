package com.test.memoapp.memo.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val title = savedStateHandle.getStateFlow(key = "title",initialValue = "")

    val selectedTime = savedStateHandle.getStateFlow<Long?>("date",null)

    val formattedDate = selectedTime.map { timestamp ->
        timestamp?.let {
            val instant = Instant.ofEpochMilli(it)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
        } ?: "선택된 날짜 없음"
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), "")

    fun onTitleChanged(newTitle: String) {
        savedStateHandle["title"] = newTitle
    }

    fun onDateSelected(newTimestamp: Long?) {
        savedStateHandle["date"] = newTimestamp
    }

}