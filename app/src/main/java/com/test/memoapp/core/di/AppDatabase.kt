package com.test.memoapp.core.di

import android.content.Context
import androidx.room.Room
import com.test.memoapp.core.data.AppDatabase
import com.test.memoapp.memo.data.MemoDao
import com.test.memoapp.memo.data.MemoTagDao
import com.test.memoapp.memo.data.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "memo_database"
        ).build()
    }

    @Provides
    fun provideMemoDao(database: AppDatabase): MemoDao {
        return database.MemoDao()
    }

    @Provides
    fun provideTagDao(database: AppDatabase) : TagDao {
        return database.TagDao()
    }

    @Provides
    fun provideMemoTagDao(database : AppDatabase) : MemoTagDao {
        return database.MemoTagDao()
    }
}