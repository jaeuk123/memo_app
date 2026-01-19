package com.test.memoapp.memo.list

import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.TagEntity
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