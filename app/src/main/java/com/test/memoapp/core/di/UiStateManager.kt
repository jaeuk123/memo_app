package com.test.memoapp.core.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class FabAction (
    val icon: @Composable () -> Unit,
    val onclick: () -> Unit,
)

data class MainUiState(
    val showBottomBar: Boolean = true,
    val fabAction: FabAction? = null
)
@HiltViewModel
class AppUiStateManager @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState : StateFlow<MainUiState> = _uiState

    fun updateBottomBarVisibility(isVisible : Boolean) {
        _uiState.value = _uiState.value.copy(showBottomBar = isVisible)
    }

    fun updateFab(action: FabAction?) {
        _uiState.value = _uiState.value.copy(fabAction = action)
    }

}