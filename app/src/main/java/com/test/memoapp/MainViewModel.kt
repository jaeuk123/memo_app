package com.test.memoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.di.TokenManager
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val repository: AuthRepository
) : ViewModel() {
    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth = _isCheckingAuth.asStateFlow()
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                val refreshToken = withTimeoutOrNull(5000) {
                    tokenManager.userFlow.map { it -> it?.refreshToken }.first()
                }
                if (refreshToken != null) {
                    _isLoggedIn.value = repository.checkToken(refreshToken)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isCheckingAuth.value = false
            }
        }
    }

}
}

}