package com.test.memoapp.core.di

import com.test.memoapp.memo.data.MemoRepository
import com.test.memoapp.memo.data.MemoRepositoryImpl
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagRepository
import com.test.memoapp.memo.data.memo_tag_relation.MemoTagRepositoryImpl
import com.test.memoapp.memo.data.tag.TagRepository
import com.test.memoapp.memo.data.tag.TagRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindTagRepository(impl: TagRepositoryImpl) : TagRepository

    @Binds
    @Singleton
    abstract fun bindMemoTagRepository(impl : MemoTagRepositoryImpl) : MemoTagRepository

}