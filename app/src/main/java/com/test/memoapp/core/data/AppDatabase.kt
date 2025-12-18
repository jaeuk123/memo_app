package com.test.memoapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.memoapp.memo.data.MemoDao
import com.test.memoapp.memo.data.MemoEntity

@Database(entities = [MemoEntity::class] , version = 1 , exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun MemoDao() : MemoDao
}