package com.dzulfaqar.quranku.core.domain.repository

import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ICoreRepository {
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkModeActive: Boolean)

    fun getAllSurah(): Flow<Resource<List<SurahDomain>>>
    fun getAllJuz(): Flow<Resource<List<JuzDomain>>>
    fun getAllAyat(): Flow<Resource<List<AyatDomain>>>
    fun getAllAyatBySurah(surah: SurahDomain?): Flow<List<AyatDomain>>
    fun getAllAyatByJuz(juz: JuzDomain?): Flow<List<AyatDomain>>

    suspend fun updateBookmark(ayat: AyatDomain, isBookmark: Boolean)
    suspend fun getAllBookmark(): Flow<List<AyatDomain>>

    fun getAllReciter(): Flow<Resource<List<ReciterDomain>>>
    fun getLatestRecitation(): Flow<ReciterDomain>
    suspend fun setLatestRecitation(latest: ReciterDomain)
    fun getRecitationByChapter(id: Int, surah: Int): Flow<Resource<List<AudioDomain>>>
}
