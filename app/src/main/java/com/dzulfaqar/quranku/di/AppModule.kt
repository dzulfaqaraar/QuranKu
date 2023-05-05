package com.dzulfaqar.quranku.di

import com.dzulfaqar.quranku.core.domain.usecase.QuranInteractor
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.core.domain.usecase.SettingInteractor
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideSettingUseCase(settingInteractor: SettingInteractor): SettingUseCase

    @Binds
    @Singleton
    abstract fun provideQuranUseCase(quranInteractor: QuranInteractor): QuranUseCase
}
