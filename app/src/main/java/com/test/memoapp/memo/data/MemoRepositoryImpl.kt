package com.test.memoapp.memo.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val tagDao: TagDao,
    private val memoTagDao: MemoTagDao,
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


    override suspend fun saveTag(tag: TagEntity) {
        tagDao.insertTag(tag)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }

    override suspend fun getMemoById(memoId: Long): MemoEntity? {
        return memoDao.getMemoById(memoId)
    }

    override suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef) {
        memoTagDao.insertMemoTagCrossRef(crossRef)
    }

    override fun getRecentModifyMemo(): Flow<List<MemoEntity>> {
        return memoDao.getRecentModifyMemo()
    }

    override fun getMemoWithTags(memoId: Long): Flow<MemoWithTags?> {
        return memoTagDao.getMemoWithTags(memoId)
    }

    override fun getTagWithMemos(tagId: Long): Flow<TagWithMemos> {
        return memoTagDao.getTagWithMemos(tagId)
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
    suspend fun saveMemo(memo: MemoEntity): Long
    fun getAllMemos(): Flow<List<MemoEntity>>
    fun getMemoByTime(startTime: Long, endTime: Long): Flow<List<MemoEntity>>
    suspend fun saveTag(tag: TagEntity)
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