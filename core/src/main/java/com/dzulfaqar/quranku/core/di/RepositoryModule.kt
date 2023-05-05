package com.dzulfaqar.quranku.core.di

import com.dzulfaqar.quranku.core.data.CoreRepository
import com.dzulfaqar.quranku.core.domain.repository.ICoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [PreferenceModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideCoreRepository(coreRepository: CoreRepository): ICoreRepository
}
