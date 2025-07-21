package com.dzulfaqar.quranku.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val settingUseCase: SettingUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
            SettingViewModel(coroutineContextProvider, settingUseCase) as T
        }
        else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
    }
}