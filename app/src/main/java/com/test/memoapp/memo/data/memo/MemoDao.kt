package com.test.memoapp.memo.data.memo

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Query("SELECT * FROM memos Where isDelete = 0 ORDER BY lastModifyTime DESC")
    fun getAllMemos() : Flow<List<MemoEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMemo(memo: MemoEntity)

    @Query("DELETE  FROM memos WHERE memoId = :memoId")
    suspend fun deleteMemo(memoId : String) : Int

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Query("SELECT * FROM memos Where memoId = :memoId And  isDelete = 0")
    fun getMemoById(memoId : String) : MemoEntity?

    @Query("SELECT * FROM memos Where scheduleTime >= :startOfDay AND scheduleTime <= :endOfDay And  isDelete = 0 ORDER BY scheduleTime ASC")
    fun getMemoByTime(startOfDay : Long , endOfDay : Long) : Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos Where isDelete = 0 ORDER BY lastModifyTime DESC LIMIT 3 ")
    fun getRecentModifyMemo() : Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos Where isDelete = 0 ORDER BY lastModifyTime DESC")
    fun getMemosPaging() : PagingSource<Int, MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMemo(memo : List<MemoEntity>)

    @Transaction
    suspend fun syncMemo(memo : List<MemoEntity>) {
        insertSyncMemo(memo)
    }

    @Query("SELECT * FROM memos Where isSync = :isSync")
    fun getNeedSyncMemos(isSync : Boolean) : Flow<List<MemoEntity>>
}