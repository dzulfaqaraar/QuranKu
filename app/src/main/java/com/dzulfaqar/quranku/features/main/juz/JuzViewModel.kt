package com.dzulfaqar.quranku.features.main.juz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.model.JuzModel
import com.dzulfaqar.quranku.utils.AppMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JuzViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModel() {

    private val _listJuz = MutableLiveData<Resource<List<JuzModel>>>()
    val listJuz: LiveData<Resource<List<JuzModel>>> = _listJuz

    init {
        getListJuz()
    }

    private fun getListJuz() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllJuz().collect {
                    _listJuz.postValue(AppMapper.mapJuzDomainToPresentation(it))
                }
            }
        }
    }
}
