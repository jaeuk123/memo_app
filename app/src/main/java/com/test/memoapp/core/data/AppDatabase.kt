package com.test.memoapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.memoapp.memo.data.MemoDao
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoTypeConverter
import com.test.memoapp.memo.data.TagDao
import com.test.memoapp.memo.data.TagEntity

@Database(entities = [MemoEntity::class, TagEntity::class] , version = 1 , exportSchema = false)
@TypeConverters(MemoTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun MemoDao() : MemoDao

    abstract fun TagDao() : TagDao
}