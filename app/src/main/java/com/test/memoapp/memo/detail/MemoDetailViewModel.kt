package com.test.memoapp.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagRepository
import com.test.memoapp.memo.data.memo_tag_relation.MemoWithTags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val memoTagRepository: MemoTagRepository,
    private val memoRepository: MemoRepository
) : ViewModel() {
    private val loadMemoId: String? = savedStateHandle["memoId"]
    val MemoWithTags: StateFlow<MemoWithTags?> = memoTagRepository.getMemoWithTags(loadMemoId!!)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = MemoWithTags(MemoEntity(title = ""), emptyList())
        )

    fun removeMemo(memoId: String) {
        viewModelScope.launch {
            memoRepository.removeMemo(memoId)
        }
    }
}