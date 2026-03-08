package com.test.memoapp.memo.lastwriteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.memoapp.memo.data.memo.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LastWriteMemoViewModel @Inject constructor(
    repository: MemoRepository
) : ViewModel() {
    val memoPagingData = repository.getPagingMemo().cachedIn(viewModelScope)
}