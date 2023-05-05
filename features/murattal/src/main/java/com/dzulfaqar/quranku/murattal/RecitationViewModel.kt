package com.dzulfaqar.quranku.murattal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.model.ReciterModel
import com.dzulfaqar.quranku.utils.AppMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecitationViewModel(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModel() {
    private val _listReciter = MutableLiveData<Resource<List<ReciterModel>>>()
    val listReciter: LiveData<Resource<List<ReciterModel>>> = _listReciter

    init {
        getListReciter()
    }

    private fun getListReciter() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllReciter().collect {
                    _listReciter.postValue(AppMapper.mapReciterDomainToPresentation(it))
                }
            }
        }
    }
}
