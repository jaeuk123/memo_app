package com.test.memoapp.memo.data.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.memoapp.memo.data.tag.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getAllTags() : Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertTag(memo: TagEntity)

    @Delete
    suspend fun deleteTag(memo : TagEntity)
}