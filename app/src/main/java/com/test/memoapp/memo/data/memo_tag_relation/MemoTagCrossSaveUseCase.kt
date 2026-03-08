package com.test.memoapp.memo.data.memo_tag_relation

import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.data.TokenManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first


class MemoTagCrossSaveUseCase @Inject constructor(
    private val repository: MemoTagRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(tags: Set<String>, saveMemoId: String) {

        val userId = tokenManager.userFlow.first()?.user
        var syncSuccess = false
        if (userId != null) {
            val memoTagCrossList =
                tags.map { tagId ->
                    MemoTagCrossDto(
                        userId = userId.id!!,
                        tagId = tagId,
                        memoId = saveMemoId
                    )
                }

            val response = repository.saveServerMemoTagCrossRef(memoTagCrossList)
            response.suspendOnSuccess {
                syncSuccess = true
            }
        }

        saveMemoLocal(tags, saveMemoId, syncSuccess)
    }

    private suspend fun saveMemoLocal(
        tags: Set<String>,
        saveMemoId: String,
        isSync: Boolean
    ) {
        tags.forEach { tagId ->
//            repository.
            repository.insertLocalMemoTagCrossRef(
                crossRef = MemoTagCrossRef(
                    tagId = tagId,
                    memoId = saveMemoId,
                    isSync = isSync
                )
            )
        }
    }
}