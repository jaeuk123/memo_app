package com.test.memoapp.memo.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateUtils
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepositoryImpl
import com.test.memoapp.memo.data.MemoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoRepositoryImpl
) : ViewModel() {
    val title = savedStateHandle.getStateFlow(key = "title", initialValue = "")
    val scheduleDate = savedStateHandle.getStateFlow<Long?>("date", null)
    val content = savedStateHandle.getStateFlow(key = "content", initialValue = "")
    val todoOption = savedStateHandle.getStateFlow(key = "todoOption", initialValue = false)

    val scheduleTime = savedStateHandle.getStateFlow<LocalTime?>("scheduleTime", null)

    val formattedDate = scheduleDate.map { timestamp ->
        timestamp?.let {
            DateUtils.convertLongToString(timestamp, DateConvertType.DEFAULT)
        } ?: "선택된 날짜 없음"
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), "")

    fun onTitleChanged(newTitle: String) {
        savedStateHandle["title"] = newTitle
    }

    fun scheduleDateSelected(newTimestamp: Long?) {
        savedStateHandle["date"] = newTimestamp
    }

    fun onContentChanged(content: String) {
        savedStateHandle["content"] = content
    }

    fun onTodoOptionChanged(options: Boolean) {
        savedStateHandle["todoOption"] = options
    }

    fun scheduleTimeSelected(newTimeStamp : LocalTime?) {
        savedStateHandle["scheduleTime"] = newTimeStamp
    }

    suspend fun saveMemo() {
        if (scheduleDate.value != null) {
            repository.saveMemo(
                MemoEntity(
                    title = title.value,
                    scheduleTime = scheduleDate.value!!,
                    memoType = MemoType.Default.value
                )
            )
        } else {
            repository.saveMemo(
                MemoEntity(
                    title = title.value,
                    scheduleTime = 0,
                    memoType = MemoType.Default.value
                )
            )
        }
    }

}