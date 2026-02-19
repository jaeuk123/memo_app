package com.test.memoapp.settings.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository : AuthRepository,
) : ViewModel() {

    fun login(email : String, password : String ) {
        if (email.isBlank() || password.isBlank()) {
            println("validation issue")
        }

        viewModelScope.launch {
            try {
                repository.login(email, password)
            } catch (e : Exception) {
                e.stackTrace
            }
        }

    }
}