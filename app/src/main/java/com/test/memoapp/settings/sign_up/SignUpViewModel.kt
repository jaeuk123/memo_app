package com.test.memoapp.settings.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {

    fun signUp(email : String, password : String ) {
        if (email.isBlank() || password.isBlank()) {
            println("validation issue")
        }

        viewModelScope.launch {
            try {
                repository.signUp(email, password)
            } catch (e : Exception) {
                e.stackTrace
            }
        }

    }
}