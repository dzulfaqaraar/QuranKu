package com.dzulfaqar.quranku.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
            BookmarkViewModel(coroutineContextProvider, quranUseCase) as T
        }
        else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
    }
}
