package com.dzulfaqar.quranku.di

import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SettingModuleDependencies {

    fun settingUseCase(): SettingUseCase
}
