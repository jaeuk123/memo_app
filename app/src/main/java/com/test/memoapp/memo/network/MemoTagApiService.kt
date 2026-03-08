package com.test.memoapp.memo.network

import com.skydoves.sandwich.ApiResponse
import com.test.memoapp.memo.data.memo.MemoDto
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagCrossDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MemoTagApiService {

    @POST("/rest/v1/memo_tag_ref")
    suspend fun saveMemoTagRelation(
        @Body relations : List<MemoTagCrossDto>
    ) :ApiResponse<Unit>

}