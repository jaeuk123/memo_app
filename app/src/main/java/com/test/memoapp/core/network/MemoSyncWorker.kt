package com.test.memoapp.core.network

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo.MemoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class MemoSyncWorker @AssistedInject constructor(
    @Assisted appContext : Context,
    @Assisted workerParams : WorkerParameters,
    private val memoRepository: MemoRepository
    ) : CoroutineWorker(appContext,workerParams) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val pageSize = 100
        var currentOffset = 0

        try {
            while (true) {
                val response = memoRepository.getSyncMemo(pageSize, currentOffset)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data.isNullOrEmpty()) {
                        break
                    } else {
                        var memoList = mutableListOf<MemoEntity>()
                        for (dto in data) {
                            memoList.add(dto.toEntity(isSync = true))
                        }
                        currentOffset += pageSize
                        memoRepository.syncMemoToLocal(memoList)
                    }
                } else {
                    Result.failure()
                    break
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}