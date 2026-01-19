package com.test.memoapp.memo.data

import kotlinx.coroutines.flow.Flow
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

    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }


}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoEntity)
    fun getAllMemos(): Flow<List<MemoEntity>>

    suspend fun saveTag(tag : TagEntity)

    fun getAllTags(): Flow<List<TagEntity>>
}