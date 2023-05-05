package com.dzulfaqar.quranku.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val settingUseCase: SettingUseCase
) : ViewModel() {
    val themeSetting = settingUseCase.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                settingUseCase.saveThemeSetting(isDarkModeActive)
            }
        }
    }
}
