package com.test.memoapp.memo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao : MemoDao,
    private val tagDao : TagDao ,
    private val memoTagDao : MemoTagDao,
    ): MemoRepository{

    override suspend fun saveMemo(memo: MemoEntity) = memoDao.insertMemo(memo)

    override fun getAllMemos() = memoDao.getAllMemos()

    override fun getMemoByTime(
        startTime: Long,
        endTime: Long
    ): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTime(startTime,endTime)
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
}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoEntity): Long
    fun getAllMemos(): Flow<List<MemoEntity>>

    fun getMemoByTime(startTime : Long , endTime : Long) : Flow<List<MemoEntity>>

    suspend fun saveTag(tag : TagEntity)

    suspend fun updateMemo(memo: MemoEntity)

    fun getAllTags(): Flow<List<TagEntity>>

    suspend fun getMemoById(memoId: Long) : MemoEntity?

    suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef)

    fun getRecentModifyMemo() : Flow<List<MemoEntity>>
}