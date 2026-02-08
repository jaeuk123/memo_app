package com.test.memoapp.memo.write

import androidx.compose.runtime.key
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepositoryImpl
import com.test.memoapp.memo.data.MemoTagCrossRef
import com.test.memoapp.memo.data.MemoType
import com.test.memoapp.memo.data.TagEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoRepositoryImpl
) : ViewModel() {
    val title = savedStateHandle.getStateFlow(key = "title", initialValue = "")
    val scheduleDate = savedStateHandle.getStateFlow("date", System.currentTimeMillis())
    val content = savedStateHandle.getStateFlow(key = "content", initialValue = "")
    val todoOption = savedStateHandle.getStateFlow(key = "todoOption", initialValue = false)
    val scheduleOption = savedStateHandle.getStateFlow(key = "scheduleOption", initialValue = false)
    val scheduleTime = savedStateHandle.getStateFlow<LocalTime>("scheduleTime", LocalTime.of(0, 0))
    val loadMemoId: Long? = savedStateHandle["memoId"]
    var allTags: StateFlow<List<TagEntity>> = repository.getAllTags().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
//    var selectTags: List<String> = savedStateHandle.getStateFlow(key = "selectTags", initialValue = emptyList)
    var selectTags: MutableStateFlow<Set<Long>> = MutableStateFlow(value = mutableSetOf())

    val formattedDate = scheduleDate.map { timestamp ->
        timestamp.let {
            DateFormatUtils.convertLongToString(timestamp, DateConvertType.DEFAULT)
        }
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), "")


    init {
        if (loadMemoId != -1L && loadMemoId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                loadData()
            }
        }
    }

    private suspend fun loadData() {
        val loadMemo = repository.getMemoById(loadMemoId!!)
        loadMemo?.let { (memoId, title, content, lastModifyTime, scheduleTime, memoType) ->
            savedStateHandle["title"] = title
            if (scheduleTime != 0L) {
                savedStateHandle["scheduleOption"] = true
                savedStateHandle["scheduleTime"] = DateFormatUtils.convertLongToTime(scheduleTime)
            }
            savedStateHandle["memoType"] = memoType
            savedStateHandle["content"] = content
        }

        repository.getMemoWithTags(loadMemoId).first()?.tags?.forEach { (tagId, tagName) ->
            selectTags.update {
                selectTags.value + tagId
            }
        }
    }

    fun handleAction(action: EventAction) {
        when (action) {
            is EventAction.onTitleChanged -> {
                savedStateHandle["title"] = action.newTitle
            }

            is EventAction.onDateChanged -> {
                savedStateHandle["date"] = action.date
            }

            is EventAction.onContentChanged -> {
                savedStateHandle["content"] = action.newContent
            }

            is EventAction.todoOptionChanged -> {
                savedStateHandle["todoOption"] = action.todoOption
            }

            is EventAction.onTimeSelected -> {
                savedStateHandle["scheduleTime"] = action.time
            }

            is EventAction.scheduleOptionChanged -> {
                savedStateHandle["scheduleOption"] = action.scheduleOption
            }

            is EventAction.saveEvent -> {
                viewModelScope.launch {
                    saveMemo()
                }
            }

            is EventAction.addTags -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addTag(action.tagName)
                }
            }
            is EventAction.tagToggle -> {
                selectTags.update {
                    if (selectTags.value.contains(action.tagId)){
                        selectTags.value - action.tagId
                    } else {
                        selectTags.value + action.tagId
                    }
                }
            }
        }
    }

    suspend fun addTag(tagName : String) {
        repository.saveTag(TagEntity(tagName = tagName))
    }
    suspend fun saveMemo() {
        var saveMemoId : Long
        if (scheduleOption.value) {
            val dateTime = LocalDateTime.of(
                DateFormatUtils.convertLongToDate(scheduleDate.value!!),
                scheduleTime.value
            )
            if (loadMemoId == -1L || loadMemoId == null) {
                saveMemoId = repository.saveMemo(
                    MemoEntity(
                        title = title.value,
                        content = content.value,
                        scheduleTime = DateFormatUtils.convertLocalDateTimeToLong(dateTime),
                        memoType = MemoType.Default.value
                    )
                )
            } else {
                saveMemoId = loadMemoId
                repository.updateMemo(
                    MemoEntity(
                        memoId = loadMemoId,
                        title = title.value,
                        content = content.value,
                        scheduleTime = DateFormatUtils.convertLocalDateTimeToLong(dateTime),
                        memoType = MemoType.Default.value
                    )
                )
            }
        } else {
            if (loadMemoId == -1L || loadMemoId == null) {
                saveMemoId = repository.saveMemo(
                    MemoEntity(
                        title = title.value,
                        content = content.value,
                        memoType = MemoType.Default.value
                    )
                )
            } else {
                saveMemoId = loadMemoId
                repository.updateMemo(
                    MemoEntity(
                        memoId = loadMemoId,
                        title = title.value,
                        content = content.value,
                        memoType = MemoType.Default.value
                    )
                )
            }
        }

        if (selectTags.value.isNotEmpty()) {
            selectTags.value.forEach { tagId ->
                repository.insertMemoTagCrossRef(crossRef = MemoTagCrossRef(tagId = tagId, memoId = saveMemoId))
            }
        }
    }
}