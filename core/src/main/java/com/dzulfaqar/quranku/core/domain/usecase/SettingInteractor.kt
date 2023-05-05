package com.dzulfaqar.quranku.core.domain.usecase

import com.dzulfaqar.quranku.core.domain.repository.ICoreRepository
import javax.inject.Inject

class SettingInteractor @Inject constructor(
    private val coreRepository: ICoreRepository
) : SettingUseCase {

    override fun getThemeSetting() = coreRepository.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        coreRepository.saveThemeSetting(isDarkModeActive)
    }
}
