package com.test.memoapp.memo.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.tag.TagEntity
import com.test.memoapp.memo.data.tag.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val saveStateHandler : SavedStateHandle,
    private val tagRepository : TagRepository,
    private val memoRepository : MemoRepository
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

    val recentModifyMemos : StateFlow<List<MemoEntity>> = memoRepository.getRecentModifyMemo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayMemos : StateFlow<List<MemoEntity>> =  memoRepository.getMemoByTime(start , end)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val tags : StateFlow<List<TagEntity>> = tagRepository.getAllTags()
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
        tagRepository.saveTag(TagEntity(tagName = createNewTag.value))
    }
}