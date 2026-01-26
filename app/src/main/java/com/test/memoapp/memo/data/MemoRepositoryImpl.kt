package com.test.memoapp.memo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao : MemoDao,
    private val tagDao : TagDao
    ): MemoRepository{

    override suspend fun saveMemo(memo: MemoEntity) = memoDao.insertMemo(memo)

    override fun getAllMemos() = memoDao.getAllMemos()

    override suspend fun saveTag(tag: TagEntity) {
        tagDao.insertTag(tag)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }

    override suspend fun getMemoById(itemId: Long): MemoEntity? {
        return memoDao.getMemoById(itemId)
    }


}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoEntity)
    fun getAllMemos(): Flow<List<MemoEntity>>

    suspend fun saveTag(tag : TagEntity)

    suspend fun updateMemo(memo: MemoEntity)

    fun getAllTags(): Flow<List<TagEntity>>

    suspend fun getMemoById(itemId: Long) : MemoEntity?
}