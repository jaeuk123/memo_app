package com.test.memoapp.core.network

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.data.TokenManager
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun signUp(email: String, pw: String): ApiResponse<AuthResponse> {
        return  authService.signUp(AuthRequest(email, pw))
    }

    suspend fun login(email: String, pw: String): ApiResponse<AuthResponse> {
        val response = authService.login(AuthRequest(email, pw))

        response.suspendOnSuccess {
            tokenManager.saveUserProfile(data)
        }

        return response
    }

    suspend fun checkToken(token: String): Boolean {
        val response = authService.refreshToken(RefreshRequest(token))
        return when (response) {
            is ApiResponse.Failure.Error -> {
                throw IllegalStateException("자동 로그인 실패: ${response.message()}")
            }
            is ApiResponse.Failure.Exception -> {
                throw response.throwable}
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