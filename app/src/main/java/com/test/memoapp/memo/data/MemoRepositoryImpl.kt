package com.test.memoapp.memo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao : MemoDao,
    ): MemoRepository{

    override suspend fun saveMemo(memo: MemoEntity) = memoDao.insertMemo(memo)

    override fun getAllMemos() = memoDao.getAllMemos()
}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoEntity)
    fun getAllMemos(): Flow<List<MemoEntity>>
}