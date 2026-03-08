package com.test.memoapp.settings.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository : AuthRepository,
) : ViewModel() {

    private val _eventChannel = Channel<LoginEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun login(email : String, password : String ) {
        if (email.isBlank() || password.isBlank()) {
            println("validation issue")
            viewModelScope.launch {
                _eventChannel.send(LoginEvent.ShowError("email 과 비밀번호를 입력해주세요"))
            }
        } else {
            viewModelScope.launch {
                try {
                    val response = repository.login(email, password)
                    response.suspendOnSuccess {
                        _eventChannel.send(LoginEvent.NavigateToHome)
                    }.suspendOnError {
                        _eventChannel.send(LoginEvent.ShowError("로그인 정보를 확인해 주세요"))
                    }.suspendOnException {
                        _eventChannel.send(LoginEvent.ShowError("네트워크 연결을 확인해 주세요"))
                    }
                } catch (e : IllegalStateException){
                    e.printStackTrace()
                }
                catch (e : Exception) {
                    println(" 네트워크를 확인해 보세요")
                }
            }
        }
    }
}

