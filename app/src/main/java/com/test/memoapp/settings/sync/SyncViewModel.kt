package com.test.memoapp.settings.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.test.memoapp.core.network.MemoSyncWorker
import com.test.memoapp.memo.data.memo.MemoDto
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SyncViewModel @Inject constructor(private val workManager: WorkManager , private val repository: MemoRepository) : ViewModel() {

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val saveRequest = OneTimeWorkRequestBuilder<MemoSyncWorker>()
        .setConstraints(constraints)
        .build()

    val syncStatus: StateFlow<String> =
        workManager.getWorkInfoByIdFlow(saveRequest.id).map { workInfo ->

            if (workInfo == null) return@map "동기화 대기 중..."

            when (workInfo?.state) {
                WorkInfo.State.ENQUEUED -> "동기화 대기 중..."
                WorkInfo.State.RUNNING -> "동기화 진행 중..."
                WorkInfo.State.SUCCEEDED -> "동기화 완료!"
                WorkInfo.State.FAILED -> "동기화 실패"
                else -> "상태: ${workInfo.state}"
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "동기화 대기중..."
        )


    val needSyncList : StateFlow<List<MemoEntity>> = repository.getNeedSyncMemo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun startSaveSync() {
        workManager.enqueueUniqueWork(
            "MEMO_SYNC_WORK",
            ExistingWorkPolicy.KEEP,
            saveRequest
        )
    }

    fun startUploadSync() {
        viewModelScope.launch {
            repository.postMemoList(needSyncList.value)
        }
    }
}