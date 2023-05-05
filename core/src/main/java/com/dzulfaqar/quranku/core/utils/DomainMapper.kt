package com.dzulfaqar.quranku.core.utils

import com.dzulfaqar.quranku.core.data.source.local.entity.*
import com.dzulfaqar.quranku.core.data.source.local.preference.Recitation
import com.dzulfaqar.quranku.core.data.source.remote.response.*
import com.dzulfaqar.quranku.core.domain.model.*

object DomainMapper {

    fun mapSurahEntitiesToDomain(input: List<SurahEntity>): List<SurahDomain> = input.map {
        SurahDomain(
            id = it.surahNumber,
            name = it.surahName,
            arabic = it.arabicName,
            totalAyat = it.versesCount
        )
    }

    fun mapSurahResponsesToEntities(input: List<SurahItemReponse>): List<SurahEntity> {
        val listResult = ArrayList<SurahEntity>()
        input.map {
            val entity = SurahEntity(
                surahNumber = it.id,
                revelationPlace = it.revelationPlace,
                revelationOrder = it.revelationOrder,
                surahName = it.nameSimple,
                arabicName = it.nameArabic,
                versesCount = it.versesCount
            )
            listResult.add(entity)
        }
        return listResult
    }

    fun mapJuzEntitiesToDomain(input: List<JuzEntity>): List<JuzDomain> = input.map {
        JuzDomain(
            number = it.juzNumber,
            firstAyat = it.firstAyat,
            lastAyat = it.lastAyat,
            totalSurat = it.totalSurah,
            totalAyat = it.totalAyat
        )
    }

    fun mapJuzResponsesToEntities(input: List<JuzItemResponse>): List<JuzEntity> {
        val listResult = ArrayList<JuzEntity>()
        input.map {
            val entity = JuzEntity(
                juzNumber = it.id,
                firstAyat = it.firstVerseId,
                lastAyat = it.lastVerseId,
                totalSurah = it.verseMapping?.size,
                totalAyat = it.versesCount
            )
            listResult.add(entity)
        }
        return listResult
    }

    fun mapJuzResponsesToEntitiesMapping(
        input: List<JuzItemResponse>,
        savedJuz: List<JuzEntity>
    ): List<JuzSurahEntity> {
        val listResult = ArrayList<JuzSurahEntity>()
        input.map {
            if (it.verseMapping != null) {
                for ((key, value) in it.verseMapping.entries) {

                    val juzId = savedJuz.find { juz -> juz.juzNumber == it.id }
                    val entity = JuzSurahEntity(
                        juzId = juzId?.juzNumber,
                        surah = key.toInt(),
                        mapping = value
                    )
                    listResult.add(entity)
                }
            }
        }
        return listResult
    }

    fun mapAyatEntitiesToDomain(input: List<AyatEntity>): List<AyatDomain> = input.map {
        AyatDomain(
            id = it.id,
            surahNumber = it.surah,
            position = it.position,
            surahName = "",
            ayatNumber = it.ayat,
            arabic = it.arabic,
            isBookmark = it.isBookmark
        )
    }

    fun mapAyatResponsesToEntities(input: List<AyatItemResponse>): List<AyatEntity> {
        val listResult = ArrayList<AyatEntity>()
        input.map {
            it.verseKey.split(":").let { verse ->
                val entity = AyatEntity(
                    position = it.id,
                    surah = verse[0].toInt(),
                    ayat = verse[1].toInt(),
                    arabic = it.textUthmani
                )
                listResult.add(entity)
            }
        }
        return listResult
    }

    fun mapAyatEntitiesToDomainBySurah(surahName: String?, input: List<AyatEntity>): List<AyatDomain> =
        input.map {
            AyatDomain(
                id = it.id,
                surahNumber = it.surah,
                position = it.position,
                surahName = surahName ?: "",
                ayatNumber = it.ayat,
                arabic = it.arabic,
                isBookmark = it.isBookmark
            )
        }

    fun mapAyatEntitiesToDomainWithListSurah(
        listSurah: Map<Int, String>,
        input: List<AyatEntity>
    ): List<AyatDomain> {
        return input.map {
            AyatDomain(
                id = it.id,
                surahNumber = it.surah,
                position = it.position,
                surahName = listSurah[it.surah] ?: "",
                ayatNumber = it.ayat,
                arabic = it.arabic,
                isBookmark = it.isBookmark
            )
        }
    }

    fun mapAyatDomainToEntity(input: AyatDomain) = AyatEntity(
        id = input.id,
        position = input.position,
        surah = input.surahNumber,
        ayat = input.ayatNumber,
        arabic = input.arabic
    )

    fun mapReciterResponsesToDomain(input: List<RecitationsItemResponse>): List<ReciterDomain> {
        val listResult = ArrayList<ReciterDomain>()
        input.map {
            val entity = ReciterDomain(
                id = it.id ?: 0,
                name = it.reciterName ?: "",
                style = it.style
            )
            listResult.add(entity)
        }
        return listResult
    }

    fun mapAudioResponsesToDomain(input: List<AudioItemResponse>): List<AudioDomain> = input.map {
        AudioDomain(
            ayat = it.verseKey ?: "",
            url = it.url ?: ""
        )
    }

    fun mapRecitationPreferenceToDomain(input: Recitation): ReciterDomain = ReciterDomain(
        id = input.id,
        name = input.name,
        style = input.style,
    )

    fun mapReciterModelToPreference(input: ReciterDomain): Recitation = Recitation(
        id = input.id,
        name = input.name,
        style = input.style,
    )
}
