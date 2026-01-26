package com.test.memoapp.memo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Query("SELECT * FROM memos ORDER BY lastModifyTime DESC")
    fun getAllMemos() : Flow<List<MemoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: MemoEntity)

    @Delete
    suspend fun deleteMemo(memo : MemoEntity)

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Query("SELECT * FROM memos Where memoId = :memoId")
    fun getMemoById(memoId : Long) : MemoEntity?
}