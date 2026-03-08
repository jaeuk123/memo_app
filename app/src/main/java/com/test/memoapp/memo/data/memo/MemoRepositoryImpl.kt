package com.test.memoapp.memo.data.memo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.data.TokenManager
import com.test.memoapp.memo.network.MemoApiService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val memoApiService: MemoApiService,
    private val tokenManager: TokenManager
) : MemoRepository {

    override suspend fun saveMemo(memoDto: MemoDto) {
        val isLoggedIn = tokenManager.isLoggedIn.first()

        if (isLoggedIn) {
            tokenManager.userFlow.first()?.user?.id?.let {
                val response = memoApiService.postMemo(memoDto.copy(userId = it))
                response.suspendOnSuccess {
                    memoDao.insertMemo(memoDto.toEntity(isSync = true))
                }.suspendOnError {
                    memoDao.insertMemo(memoDto.toEntity(false))
                }.suspendOnFailure {
                    memoDao.insertMemo(memoDto.toEntity(false))
                }
            }
        } else {
            memoDao.insertMemo(memoDto.toEntity(false))
        }
    }

    override suspend fun postMemoList(list: List<MemoEntity>) {
        val user = tokenManager.userFlow.first()
        val postList = mutableListOf<MemoDto>()
        val saveList = mutableListOf<MemoEntity>()
        if (user != null) {
            for (entity in list) {
                postList.add(entity.toDto(userId = user.user?.id!!))
                saveList.add(entity.copy(isSync = true))
            }
        }
        val response = memoApiService.postMemoList(postList)

        syncMemoToLocal(saveList)

        response.suspendOnSuccess {
            syncMemoToLocal(saveList)
        }
    }

    override fun getAllMemos() = memoDao.getAllMemos()

    override fun getMemoByTime(
        startTime: Long,
        endTime: Long
    ): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTime(startTime, endTime)
    }

    override suspend fun removeMemo(memo: MemoEntity) {
        val isLoggedIn = tokenManager.isLoggedIn.first()

        if (isLoggedIn) {
            val response = memoApiService.deleteMemo("eq.${memo.memoId}")
            response.suspendOnSuccess {
                memoDao.deleteMemo(memo.memoId)
            }.suspendOnError {
                memoDao.updateMemo(memo.copy(isSync = false , isDelete = true))
            }.suspendOnFailure {
                memoDao.updateMemo(memo.copy(isSync = false , isDelete = true))
            }
        } else {
            memoDao.deleteMemo(memo.memoId)
        }

    }

    override suspend fun updateMemo(memo: MemoDto) {
        val isLoggedIn = tokenManager.isLoggedIn.first()

        if (isLoggedIn) {
            val memoUpdateDto = MemoUpdateDto(
                content = memo.content,
                title = memo.title,
                scheduleTime = memo.scheduleTime
            )
            val response = memoApiService.updateMemo(memoUpdateDto,idFilter = "eq.${memo.memoId}")
            response.suspendOnSuccess {
                memoDao.updateMemo(memo.toEntity(true))
            }.suspendOnError {
                memoDao.updateMemo(memo.toEntity(false))
            }.suspendOnFailure {
                memoDao.updateMemo(memo.toEntity(false))
            }
        } else {
            memoDao.updateMemo(memo.toEntity(false))
        }
    }

    override suspend fun getMemoById(memoId: String): MemoEntity? {
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

    override suspend fun getSyncMemo(limit : Int, currentOffset : Int) : Response<List<MemoDto>> {
        val response = memoApiService.getSyncMemo(limit = limit, currentOffset)
        return response
    }

    override suspend fun syncMemoToLocal(syncMemo : List<MemoEntity>) {
        memoDao.syncMemo(syncMemo)
    }

    override fun getNeedSyncMemo() : Flow<List<MemoEntity>> {
        return memoDao.getNeedSyncMemos(false)
    }

}

interface MemoRepository {
    suspend fun saveMemo(memo: MemoDto)
    suspend fun postMemoList(list : List<MemoEntity>)
    fun getAllMemos(): Flow<List<MemoEntity>>
    fun getMemoByTime(startTime: Long, endTime: Long): Flow<List<MemoEntity>>
    suspend fun updateMemo(memo: MemoDto)
    suspend fun getMemoById(memoId: String): MemoEntity?
    fun getRecentModifyMemo(): Flow<List<MemoEntity>>
    suspend fun removeMemo(memoId: MemoEntity)
    fun getPagingMemo(): Flow<PagingData<MemoEntity>>

    suspend fun getSyncMemo(limit: Int,currentOffset: Int) : Response<List<MemoDto>>

    suspend fun syncMemoToLocal(syncMemo : List<MemoEntity>)

    fun getNeedSyncMemo() : Flow<List<MemoEntity>>
}