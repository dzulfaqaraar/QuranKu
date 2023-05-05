package com.dzulfaqar.quranku.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingUseCase: SettingUseCase
) : ViewModel() {
    val themeSetting = settingUseCase.getThemeSetting().asLiveData()
}
