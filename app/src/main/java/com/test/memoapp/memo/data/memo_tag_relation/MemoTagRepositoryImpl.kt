package com.test.memoapp.memo.data.memo_tag_relation

import com.skydoves.sandwich.ApiResponse
import com.test.memoapp.memo.network.MemoTagApiService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow



class MemoTagRepositoryImpl @Inject constructor(
    private val memoTagDao: MemoTagDao,
    private val tagMemoApiService: MemoTagApiService,
) : MemoTagRepository {
    override suspend fun insertLocalMemoTagCrossRef(crossRef: MemoTagCrossRef) {
        memoTagDao.insertMemoTagCrossRef(crossRef)
    }
    override suspend fun saveServerMemoTagCrossRef(list : List<MemoTagCrossDto>): ApiResponse<Unit> {
        return tagMemoApiService.saveMemoTagRelation(list)
    }
    override fun getMemoWithTags(memoId: String): Flow<MemoWithTags?> {
        return memoTagDao.getMemoWithTags(memoId)
    }

    override fun getTagWithMemos(tagId: String): Flow<TagWithMemos> {
        return memoTagDao.getTagWithMemos(tagId)
    }
}

interface MemoTagRepository {
    suspend fun insertLocalMemoTagCrossRef(crossRef: MemoTagCrossRef)

    fun getMemoWithTags(memoId: String): Flow<MemoWithTags?>

    fun getTagWithMemos(tagId: String): Flow<TagWithMemos>

    suspend fun saveServerMemoTagCrossRef(list : List<MemoTagCrossDto>): ApiResponse<Unit>
}