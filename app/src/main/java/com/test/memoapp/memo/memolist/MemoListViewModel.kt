package com.test.memoapp.memo.memolist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagRepository
import com.test.memoapp.memo.data.memo_tag_relation.TagWithMemos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoTagRepository
) : ViewModel() {
    val loadTagId: StateFlow<Long?> =
        savedStateHandle.getStateFlow(key = "tagId", initialValue = null)

    val tagWithMemos: StateFlow<TagWithMemos?> = loadTagId
        .flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                repository.getTagWithMemos(id)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

}