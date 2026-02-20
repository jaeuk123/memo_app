package com.test.memoapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.memoapp.memo.data.memo.MemoDao
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagCrossRef
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagDao
import com.test.memoapp.memo.data.memo.MemoTypeConverter
import com.test.memoapp.memo.data.tag.TagDao
import com.test.memoapp.memo.data.tag.TagEntity

@Database(entities = [MemoEntity::class, TagEntity::class , MemoTagCrossRef::class] , version = 1 , exportSchema = false)
@TypeConverters(MemoTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun MemoDao() : MemoDao

    abstract fun TagDao() : TagDao

    abstract fun MemoTagDao() : MemoTagDao
}