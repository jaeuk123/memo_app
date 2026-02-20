package com.test.memoapp.core.network

import com.google.gson.annotations.SerializedName
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    //로그인
    @POST("/auth/v1/token?grant_type=password")
    suspend fun login(
        @Body request : AuthRequest,
    ) : ApiResponse<AuthResponse>

    //회원 가입
    @POST("/auth/v1/signup")
    suspend fun signUp(
        @Body request : AuthRequest
    ) : ApiResponse<AuthResponse>

    //리프레시 토큰 체크
    @POST("/auth/v1/token?grant_type=refresh_token")
    suspend fun refreshToken(
        @Body refreshRequest: RefreshRequest
    ) : ApiResponse<AuthResponse>

    @POST("/auth/v1/logout")
    suspend fun logout() : ApiResponse<Unit>
}

data class AuthRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class RefreshRequest(val refresh_token: String)

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("user") val user: UserInfo
)

data class UserInfo(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String
)
