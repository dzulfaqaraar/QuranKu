package com.dzulfaqar.quranku.di

import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BookmarkModuleDependencies {

    fun quranUseCase(): QuranUseCase
    fun coroutineContextProvider(): CoroutineContextProvider
}
