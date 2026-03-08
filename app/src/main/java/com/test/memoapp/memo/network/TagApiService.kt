package com.test.memoapp.memo.network

import com.skydoves.sandwich.ApiResponse
import com.test.memoapp.memo.data.memo.MemoDto
import com.test.memoapp.memo.data.tag.TagDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TagApiService {

    @POST("/rest/v1/tags")
    suspend fun saveTag(
        @Body tag : TagDto
    ) :ApiResponse<Unit>

    @POST("rest/v1/tags")
    suspend fun getSyncTag(
        @Query("limit") limit :Int,
        @Query("offset") offset: Int,
        @Query("select") select : String = "*"
    ) : Response<List<TagDto>>

}