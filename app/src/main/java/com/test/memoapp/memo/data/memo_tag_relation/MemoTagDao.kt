package com.test.memoapp.memo.data.memo_tag_relation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoTagDao {
    // 1. 데이터 삽입/삭제
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMemoTagCrossRef(crossRef: MemoTagCrossRef)
    @Delete
    suspend fun deleteMemoTagCrossRef(crossRef: MemoTagCrossRef)
    // 2. Item에 연결된 모든 Tag 조회
    @Transaction
    @Query("SELECT * FROM memos WHERE memoId = :memoId")
    fun getMemoWithTags(memoId: String): Flow<MemoWithTags?>

    // 3. Tag에 연결된 모든 Item 조회
    @Transaction
    @Query("SELECT * FROM tags WHERE tagId = :tagId")
    fun getTagWithMemos(tagId: String): Flow<TagWithMemos>
}