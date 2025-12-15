package com.test.memoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.memoapp.data.Memo.MemoDao
import com.test.memoapp.data.Memo.MemoEntity

@Database(entities = [MemoEntity::class] , version = 1 , exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun MemoDao() : MemoDao
}