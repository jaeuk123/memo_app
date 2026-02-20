package com.test.memoapp.memo.data.tag

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
) : TagRepository {
    override suspend fun saveTag(tag: TagEntity) {
        tagDao.insertTag(tag)
    }
    override fun getAllTags(): Flow<List<TagEntity>> {
        return tagDao.getAllTags()
    }
}

interface TagRepository {
    suspend fun saveTag(tag: TagEntity)
    fun getAllTags(): Flow<List<TagEntity>>
}