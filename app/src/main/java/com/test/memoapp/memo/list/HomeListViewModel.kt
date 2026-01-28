package com.test.memoapp.memo.list

import android.text.format.DateUtils
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.core.util.TimeUtils
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.TagEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val saveStateHandler : SavedStateHandle ,
    private val repository : MemoRepository
) : ViewModel() {

    val start = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val end = LocalDate.now()
        .plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val memos : StateFlow<List<MemoEntity>> =  repository.getMemoByTime(start , end)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val tags : StateFlow<List<TagEntity>> = repository.getAllTags()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val createNewTag = saveStateHandler.getStateFlow(key = "createNewTag", initialValue = "")

    fun changeWriteTag(tag : String) {
        saveStateHandler["createNewTag"] = tag
    }

    suspend fun saveTag() {
        repository.saveTag(TagEntity(tagName = createNewTag.value))
    }
}