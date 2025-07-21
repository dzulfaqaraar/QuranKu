package com.dzulfaqar.quranku.features.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzulfaqar.quranku.core.coroutine.CoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.core.utils.Constant
import com.dzulfaqar.quranku.core.utils.Event
import com.dzulfaqar.quranku.model.*
import com.dzulfaqar.quranku.utils.AppMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val quranUseCase: QuranUseCase
) : ViewModel() {

    private val _listAyatState = MutableLiveData<Resource<List<AyatModel>>>()
    val listAyatState: LiveData<Resource<List<AyatModel>>> = _listAyatState

    private val _latestRecitation = MutableLiveData<ReciterModel>()
    val latestRecitation: LiveData<ReciterModel> = _latestRecitation

    private val _listAyatData = MutableLiveData<List<AyatModel>>()
    val listAyatData: LiveData<List<AyatModel>> = _listAyatData

    private val _listAudioFiles = MutableLiveData<Resource<ArrayList<String>>>()
    val listAudioFiles: LiveData<Resource<ArrayList<String>>> = _listAudioFiles

    private val _eventAudioPlaying = MutableLiveData<Event<Boolean>>()
    val eventAudioPlaying: LiveData<Event<Boolean>> = _eventAudioPlaying

    var surah: SurahModel? = null
    var juz: JuzModel? = null
    var isFromSurah: Boolean? = null
    var selectedReciter: ReciterModel? = null

    var requiredDownload = arrayListOf<String>()
    var isAudioPaused = false

    init {
        getListAyat()
        getLatestRecitation()
    }

    private fun getListAyat() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllAyat().collect { data ->
                    _listAyatState.postValue(AppMapper.mapAyatDomainToPresentation(data))
                }
            }
        }
    }

    private fun getLatestRecitation() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getLatestRecitation().collect { data ->
                    _latestRecitation.postValue(AppMapper.mapReciterDomainToPresentation(data))
                }
            }
        }
    }

    fun getAllAyatBySurah(surah: SurahModel?) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllAyatBySurah(AppMapper.mapSurahModelToDomain(surah)).collect { data ->
                    _listAyatData.postValue(AppMapper.mapAyatDomainToPresentation(data))
                }
            }
        }
    }

    fun getAllAyatByJuz(juz: JuzModel?) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getAllAyatByJuz(AppMapper.mapJuzModelToDomain(juz)).collect { data ->
                    _listAyatData.postValue(AppMapper.mapAyatDomainToPresentation(data))
                }
            }
        }
    }

    fun bookmarkAyat(ayat: AyatModel, isBookmark: Boolean) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.updateBookmark(AppMapper.mapAyatModelToDomain(ayat), isBookmark)
            }
        }
    }

    fun setLatestRecitation() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                selectedReciter?.let {
                    quranUseCase.setLatestRecitation(AppMapper.mapReciterModelToDomain(it))
                }
            }
        }
    }

    fun getRecitationByChapter(id: Int, surah: Int) {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                quranUseCase.getRecitationByChapter(id, surah).collect { data ->
                    _listAudioFiles.postValue(AppMapper.mapAudioDomainToPresentation(data))
                }
            }
        }
    }

    fun playAudioPlayer(listAudio: ArrayList<String>?) {
        requiredDownload = listAudio ?: arrayListOf()
        _eventAudioPlaying.value = Event(true)
    }

    fun resumeAudioPlayer() {
        isAudioPaused = false
    }

    fun pauseAudioPlayer() {
        _eventAudioPlaying.value = Event(false)
        isAudioPaused = true
    }

    fun resetPlayer() {
        _eventAudioPlaying.value = Event(false)
        isAudioPaused = false
    }
}
