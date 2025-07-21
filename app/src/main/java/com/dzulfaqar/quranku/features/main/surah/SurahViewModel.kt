package com.dzulfaqar.quranku.features.main.surah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.model.SurahModel
import com.dzulfaqar.quranku.utils.AppMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModel() {
    private val _listSurah = MutableLiveData<Resource<List<SurahModel>>>()
    val listSurah: LiveData<Resource<List<SurahModel>>> = _listSurah

    init {
        getListSurah()
    }

    private fun getListSurah() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllSurah().collect { data ->
                    _listSurah.postValue(AppMapper.mapSurahDomainToPresentation(data))
                }
            }
        }
    }
}
