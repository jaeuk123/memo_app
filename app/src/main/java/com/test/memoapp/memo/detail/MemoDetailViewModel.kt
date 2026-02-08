package com.test.memoapp.memo.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.MemoWithTags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MemoRepository
) : ViewModel() {
    val loadMemoId: Long = savedStateHandle["memoId"] ?: 0L
    val MemoWithTags : StateFlow<MemoWithTags?> = repository.getMemoWithTags(loadMemoId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = MemoWithTags(MemoEntity(title = "", memoType = ""),emptyList())
        )

    fun removeMemo(memoId : Long) {
        viewModelScope.launch {
            repository.removeMemo(memoId)
        }
    }
}