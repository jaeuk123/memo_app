package com.test.memoapp.settings.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.retrofit.apiMessage
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


sealed class SignUpEvent {
    object SuccessEvent : SignUpEvent()
    data class ShowError(val message: String) : SignUpEvent()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {

    private val _eventChannel = Channel<SignUpEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            println("validation issue")
        }

        viewModelScope.launch {
            try {
                val response = repository.signUp(email, password)
                response.suspendOnSuccess {
                    _eventChannel.send(SignUpEvent.SuccessEvent)
                }.suspendOnError {
                    _eventChannel.send(SignUpEvent.ShowError("데이터 형식을 맞춰 주세요"))
                }.suspendOnFailure {
                    _eventChannel.send(SignUpEvent.ShowError("네트워크 연결을 확인해주세요"))
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

    }
}