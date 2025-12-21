package com.test.memoapp.core.di

import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.MemoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMemoRepository(impl : MemoRepositoryImpl) : MemoRepository
}