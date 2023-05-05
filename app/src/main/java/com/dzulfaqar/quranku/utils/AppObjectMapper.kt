package com.dzulfaqar.quranku.utils

import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.*
import com.dzulfaqar.quranku.model.*

inline fun <IN, OUT> Resource<IN>.mapResource(
    callback: (IN?) -> OUT
): Resource<OUT> {
    return when (this) {
        is Resource.Loading -> Resource.Loading()
        is Resource.Error -> Resource.Error(message ?: "")
        else -> Resource.Success(callback(data))
    }
}

fun SurahDomain.mapToPresentation(): SurahModel {
    return SurahModel(
        id = id,
        name = name,
        arabic = arabic,
        totalAyat = totalAyat
    )
}

fun JuzDomain.mapToPresentation(): JuzModel {
    return JuzModel(
        number = number,
        firstAyat = firstAyat,
        lastAyat = lastAyat,
        totalSurat = totalSurat,
        totalAyat = totalAyat
    )
}

fun AyatDomain.mapToPresentation(): AyatModel {
    return AyatModel(
        id = id,
        surahNumber = surahNumber,
        position = position,
        surahName = surahName,
        ayatNumber = ayatNumber,
        arabic = arabic,
        isBookmark = isBookmark
    )
}

fun ReciterDomain.mapToPresentation(): ReciterModel {
    return ReciterModel(
        id = id,
        name = name,
        style = style
    )
}

fun SurahModel.mapToDomain(): SurahDomain {
    return SurahDomain(
        id = id,
        name = name,
        arabic = arabic,
        totalAyat = totalAyat
    )
}

fun JuzModel.mapToDomain(): JuzDomain {
    return JuzDomain(
        number = number,
        firstAyat = firstAyat,
        lastAyat = lastAyat,
        totalSurat = totalSurat,
        totalAyat = totalAyat
    )
}

fun AyatModel.mapToDomain(): AyatDomain {
    return AyatDomain(
        id = id,
        surahNumber = surahNumber,
        position = position,
        surahName = surahName,
        ayatNumber = ayatNumber,
        arabic = arabic,
        isBookmark = isBookmark
    )
}

fun ReciterModel.mapToDomain(): ReciterDomain {
    return ReciterDomain(
        id = id,
        name = name,
        style = style
    )
}
