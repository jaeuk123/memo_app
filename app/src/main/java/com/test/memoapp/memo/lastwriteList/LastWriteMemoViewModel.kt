package com.test.memoapp.memo.lastwriteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.memoapp.memo.data.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LastWriteMemoViewModel @Inject constructor(
    repository: MemoRepository
) : ViewModel() {
    val memoPagingData = repository.getPagingMemo().cachedIn(viewModelScope)
}