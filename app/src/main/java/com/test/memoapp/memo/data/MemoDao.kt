package com.test.memoapp.memo.data

import androidx.paging.PagingSource
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
    suspend fun insertMemo(memo: MemoEntity) : Long

    @Query("DELETE  FROM memos WHERE memoId = :memoId")
    suspend fun deleteMemo(memoId : Long) : Int

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Query("SELECT * FROM memos Where memoId = :memoId")
    fun getMemoById(memoId : Long) : MemoEntity?

    @Query("SELECT * FROM memos Where scheduleTime >= :startOfDay AND scheduleTime <= :endOfDay ORDER BY scheduleTime ASC")
    fun getMemoByTime(startOfDay : Long , endOfDay : Long) : Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos ORDER BY lastModifyTime DESC LIMIT 3")
    fun getRecentModifyMemo() : Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos ORDER BY lastModifyTime DESC")
    fun getMemosPaging() : PagingSource<Int , MemoEntity>
}