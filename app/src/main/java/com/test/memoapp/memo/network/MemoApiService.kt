package com.test.memoapp.memo.network

import com.skydoves.sandwich.ApiResponse
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagCrossRef
import com.test.memoapp.memo.data.tag.TagEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface MemoApiService {
    //단건 메모 데이터 전송
    @POST("/rest/v1/memos")
    suspend fun postMemo(
        @Body memo : MemoEntity
    ) :ApiResponse<Unit>

    //단건 태그 데이터 전송
    @POST("/rest/v1/tags")
    suspend fun postTag (
        @Body tag : TagEntity
    ) : ApiResponse<Unit>

    //관계 전송
    @POST("/rest/v1/memo_tag_ref")
    suspend fun postRef (
        @Body memoTagRef : MemoTagCrossRef
    ) : ApiResponse<Unit>
}