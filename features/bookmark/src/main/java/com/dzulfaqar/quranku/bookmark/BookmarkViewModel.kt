package com.dzulfaqar.quranku.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.core.utils.Event
import com.dzulfaqar.quranku.model.AyatModel
import com.dzulfaqar.quranku.utils.AppMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModel() {
    private val _listBookmark = MutableLiveData<List<AyatModel>>()
    val listBookmark: LiveData<List<AyatModel>> = _listBookmark

    private val _deletedData = MutableLiveData<Event<AyatModel>>()
    val deletedData: LiveData<Event<AyatModel>> = _deletedData

    init {
        getListBookmark()
    }

    private fun getListBookmark() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllBookmark().collect {
                    _listBookmark.postValue(AppMapper.mapAyatDomainToPresentation(it))
                }
            }
        }
    }

    fun bookmarkAyat(ayat: AyatModel, isBookmark: Boolean, askCanceling: Boolean = true) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.updateBookmark(AppMapper.mapAyatModelToDomain(ayat), isBookmark)

                if (askCanceling) _deletedData.postValue(Event(ayat))
            }

            getListBookmark()
        }
    }
}
