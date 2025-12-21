package com.test.memoapp.memo.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val saveStateHandler : SavedStateHandle ,
    private val repository : MemoRepository
) : ViewModel() {
    val memos : StateFlow<List<MemoEntity>> =  repository.getAllMemos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}