package com.test.memoapp.memo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.memoapp.memo.data.memo.MemoDao
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagCrossRef
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagDao
import com.test.memoapp.memo.data.memo_tag_relation.MemoWithTags
import com.test.memoapp.memo.data.memo_tag_relation.TagWithMemos
import com.test.memoapp.memo.data.tag.TagDao
import com.test.memoapp.memo.data.tag.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
) : MemoRepository {

    override suspend fun saveMemo(memo: MemoEntity) = memoDao.insertMemo(memo)

    override fun getAllMemos() = memoDao.getAllMemos()

    override fun getMemoByTime(
        startTime: Long,
        endTime: Long
    ): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTime(startTime, endTime)
    }

    override suspend fun removeMemo(memoId: Long): Int {
        return memoDao.deleteMemo(memoId)
    }




    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }


    override suspend fun getMemoById(memoId: Long): MemoEntity? {
        return memoDao.getMemoById(memoId)
    }



    override fun getRecentModifyMemo(): Flow<List<MemoEntity>> {
        return memoDao.getRecentModifyMemo()
    }

    override fun getPagingMemo() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            prefetchDistance = 2
        ),
        pagingSourceFactory = { memoDao.getMemosPaging() }
    ).flow

}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoEntity)
    fun getAllMemos(): Flow<List<MemoEntity>>
    fun getMemoByTime(startTime: Long, endTime: Long): Flow<List<MemoEntity>>
    suspend fun updateMemo(memo: MemoEntity)
    fun getAllTags(): Flow<List<TagEntity>>
    suspend fun getMemoById(memoId: Long): MemoEntity?
    suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef)
    fun getRecentModifyMemo(): Flow<List<MemoEntity>>
    fun getMemoWithTags(memoId: Long): Flow<MemoWithTags?>
    suspend fun removeMemo(memoId: Long): Int
    fun getTagWithMemos(tagId: Long): Flow<TagWithMemos>

    fun getPagingMemo() : Flow<PagingData<MemoEntity>>
}