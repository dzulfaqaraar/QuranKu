package com.dzulfaqar.quranku.core.data.source.local

import com.dzulfaqar.quranku.core.data.source.local.entity.*
import com.dzulfaqar.quranku.core.data.source.local.preference.CorePreferences
import com.dzulfaqar.quranku.core.data.source.local.preference.Recitation
import com.dzulfaqar.quranku.core.data.source.local.room.AyatDao
import com.dzulfaqar.quranku.core.data.source.local.room.JuzDao
import com.dzulfaqar.quranku.core.data.source.local.room.SurahDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val corePreferences: CorePreferences,
    private val surahDao: SurahDao,
    private val juzDao: JuzDao,
    private val ayatDao: AyatDao
) {
    fun getThemeSetting() = corePreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        corePreferences.saveThemeSetting(isDarkModeActive)

    fun getAllSurah() = surahDao.getAllSurah()

    suspend fun getSurahByIds(ids: List<Int>) = surahDao.getSurahByIds(ids)

    suspend fun insertListSurah(listSurah: List<SurahEntity>) {
        surahDao.deleteListSurah()
        surahDao.insertListSurah(listSurah)
    }

    fun getAllJuz() = juzDao.getAllJuz()

    suspend fun insertListJuz(listJuz: List<JuzEntity>) {
        juzDao.deleteListJuz()
        juzDao.insertListJuz(listJuz)
    }

    suspend fun insertListJuzSurah(listJuzSurah: List<JuzSurahEntity>) {
        juzDao.deleteListJuzSurah()
        juzDao.insertListJuzSurah(listJuzSurah)
    }

    suspend fun getJuzSurah(juzId: Int?) = juzDao.getJuzSurah(juzId)

    fun getAllAyat() = ayatDao.getAllAyat()

    suspend fun insertListAyat(listAyat: List<AyatEntity>) {
        ayatDao.deleteListAyat()
        ayatDao.insertListAyat(listAyat)
    }

    fun getAllAyatBySurah(id: Int?) = ayatDao.getAllAyatBySurah(id)

    suspend fun getAllAyatByJuz(firstAyat: Int?, lastAyat: Int?) =
        ayatDao.getAllAyatByJuz(firstAyat, lastAyat)

    suspend fun updateBookmark(ayat: AyatEntity, isBookmark: Boolean) {
        ayat.isBookmark = isBookmark
        ayatDao.updateBookmark(ayat)
    }

    fun getAllBookmark() = ayatDao.getAllBookmark()

    fun getLatestRecitation() = corePreferences.getLatestRecitation()

    suspend fun setLatestRecitation(latest: Recitation) = corePreferences.setLatestRecitation(latest)
}
