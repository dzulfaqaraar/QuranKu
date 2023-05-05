package com.dzulfaqar.quranku.core.domain.usecase

import com.dzulfaqar.quranku.core.domain.model.AyatDomain
import com.dzulfaqar.quranku.core.domain.model.JuzDomain
import com.dzulfaqar.quranku.core.domain.model.ReciterDomain
import com.dzulfaqar.quranku.core.domain.model.SurahDomain
import com.dzulfaqar.quranku.core.domain.repository.ICoreRepository
import javax.inject.Inject

class QuranInteractor @Inject constructor(
    private val coreRepository: ICoreRepository
) : QuranUseCase {
    override fun getAllSurah() = coreRepository.getAllSurah()
    override fun getAllJuz() = coreRepository.getAllJuz()
    override fun getAllAyat() = coreRepository.getAllAyat()
    override fun getAllAyatBySurah(surah: SurahDomain?) = coreRepository.getAllAyatBySurah(surah)
    override fun getAllAyatByJuz(juz: JuzDomain?) = coreRepository.getAllAyatByJuz(juz)
    override suspend fun updateBookmark(ayat: AyatDomain, isBookmark: Boolean) =
        coreRepository.updateBookmark(ayat, isBookmark)

    override suspend fun getAllBookmark() = coreRepository.getAllBookmark()
    override fun getAllReciter() = coreRepository.getAllReciter()
    override fun getLatestRecitation() = coreRepository.getLatestRecitation()
    override suspend fun setLatestRecitation(latest: ReciterDomain) =
        coreRepository.setLatestRecitation(latest)

    override fun getRecitationByChapter(id: Int, surah: Int) =
        coreRepository.getRecitationByChapter(id, surah)
}
