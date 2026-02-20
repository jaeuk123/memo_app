package com.test.memoapp.memo.data.memo_tag_relation

import com.test.memoapp.memo.data.memo.MemoDao
import com.test.memoapp.memo.data.tag.TagDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class MemoTagRepositoryImpl @Inject constructor(
    private val memoTagDao: MemoTagDao
) : MemoTagRepository {
    override suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef) {
        memoTagDao.insertMemoTagCrossRef(crossRef)
    }
    override fun getMemoWithTags(memoId: String): Flow<MemoWithTags?> {
        return memoTagDao.getMemoWithTags(memoId)
    }

    override fun getTagWithMemos(tagId: String): Flow<TagWithMemos> {
        return memoTagDao.getTagWithMemos(tagId)
    }

}

interface MemoTagRepository {
    suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef)

    fun getMemoWithTags(memoId: String): Flow<MemoWithTags?>

    fun getTagWithMemos(tagId: String): Flow<TagWithMemos>
}