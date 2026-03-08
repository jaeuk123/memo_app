package com.test.memoapp.memo.network

import com.skydoves.sandwich.ApiResponse
import com.test.memoapp.memo.data.memo.MemoDto
import com.test.memoapp.memo.data.memo.MemoUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MemoApiService {
    //단건 메모 데이터 전송
    @POST("/rest/v1/memos")
    suspend fun postMemo(
        @Body memo: MemoDto
    ): ApiResponse<Unit>


    @POST("rest/v1/memos")
    suspend fun postMemoList(
        @Body memo: List<MemoDto>
    ): ApiResponse<Unit>

    @PATCH("/rest/v1/memos")
    suspend fun updateMemo(
        @Body updateData: MemoUpdateDto,
        @Query("id") idFilter: String,
    ): ApiResponse<Unit>

    @DELETE("rest/v1/memos")
    suspend fun deleteMemo(
        @Query("id") idFilter: String
    ): ApiResponse<Unit>

    @GET("rest/v1/memos")
    suspend fun getSyncMemo(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("select") select: String = "*"
    ): Response<List<MemoDto>>

}