package com.test.memoapp.core.network

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.test.memoapp.core.data.TokenManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun signUp(email: String, pw: String): String {
        val response = authService.signUp(AuthRequest(email, pw))

        when (response) {
            is ApiResponse.Failure.Error -> throw IllegalStateException("회원 가입 실패: ${response.message()}")
            is ApiResponse.Failure.Exception -> throw response.throwable
            is ApiResponse.Success -> return "회원 가입 성공"
        }
    }

    suspend fun login(email: String, pw: String) {
        val response = authService.login(AuthRequest(email, pw))

        when (response) {
            is ApiResponse.Failure.Error -> throw IllegalStateException("로그인 실패: ${response.message()}")
            is ApiResponse.Failure.Exception -> throw response.throwable
            is ApiResponse.Success -> {
                tokenManager.saveUserProfile(response.data)
            }
        }
    }

    suspend fun checkToken(token: String): Boolean {
        val response = authService.refreshToken(RefreshRequest(token))
        return when (response) {
            is ApiResponse.Failure.Error -> {
                throw IllegalStateException("자동 로그인 실패: ${response.message()}")
            }

            is ApiResponse.Failure.Exception -> throw response.throwable
            is ApiResponse.Success -> {
                response.let {
                    tokenManager.saveUserProfile(response.data)
                }
                true
            }
        }
    }

    suspend fun logout(): Boolean {
        return when(authService.logout()){
            is ApiResponse.Failure.Error -> false
            is ApiResponse.Failure.Exception -> false
            is ApiResponse.Success<*> -> true
        }
    }
}