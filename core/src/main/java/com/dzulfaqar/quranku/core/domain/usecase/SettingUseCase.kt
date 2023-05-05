package com.dzulfaqar.quranku.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingUseCase {
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
}
