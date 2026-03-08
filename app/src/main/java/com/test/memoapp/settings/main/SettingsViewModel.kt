package com.test.memoapp.settings.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.data.TokenManager
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
    private val repository: AuthRepository
) : ViewModel() {

    val email: StateFlow<String> = tokenManager.userFlow
        .map { it?.user?.email ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isLoggedIn: StateFlow<Boolean> = email
        .map {
            it.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun logout() {
        viewModelScope.launch {
            val logout = repository.logout()
            tokenManager.clearToken()
            if (logout) {
                tokenManager.clearToken()
            }
        }
    }

}