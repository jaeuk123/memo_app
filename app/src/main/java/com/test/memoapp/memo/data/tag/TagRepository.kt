package com.test.memoapp.memo.data.tag

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.test.memoapp.core.data.TokenManager
import com.test.memoapp.memo.network.TagApiService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val tagApiService: TagApiService,
    private val tokenManager: TokenManager
) : TagRepository {
    override suspend fun saveTag(tag: TagEntity) {
        val user = tokenManager.userFlow.first()?.user
        if (user != null) {
            val response = tagApiService.saveTag(
                TagDto(
                    tagId = tag.tagId,
                    tagName = tag.tagName,
                    userId = user.id!!
                )
            )

            response.suspendOnSuccess {
                tagDao.insertTag(tag.copy(isSync = true))
            }.suspendOnError {
                tagDao.insertTag(tag)
            }.suspendOnFailure {
                tagDao.insertTag(tag)
            }
        } else {
            tagDao.insertTag(tag)
        }
    }

    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }

    override suspend fun getSyncMemo(pageSize : Int, currentOffset : Int): Response<List<TagDto>> {
        return tagApiService.getSyncTag(limit = pageSize, offset = currentOffset)
    }

    override suspend fun syncTagToLocal(list : List<TagEntity>) {
        tagDao.syncTag(list)
    }
}

interface TagRepository {
    suspend fun saveTag(tag: TagEntity)
    fun getAllTags(): Flow<List<TagEntity>>

    suspend fun getSyncMemo(pageSize : Int, currentOffset : Int): Response<List<TagDto>>

    suspend fun syncTagToLocal(list : List<TagEntity>)
}