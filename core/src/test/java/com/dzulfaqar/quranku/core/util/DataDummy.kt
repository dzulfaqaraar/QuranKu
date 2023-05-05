package com.dzulfaqar.quranku.core.util

import com.dzulfaqar.quranku.core.data.source.remote.response.AyatItemResponse
import com.dzulfaqar.quranku.core.data.source.remote.response.JuzItemResponse
import com.dzulfaqar.quranku.core.data.source.remote.response.SurahItemReponse

object DataDummy {
    fun listSurahReponse(): List<SurahItemReponse> {
        return arrayListOf(
            SurahItemReponse(
                id = 1,
                revelationPlace = "makkah",
                revelationOrder = 5,
                nameSimple = "Al-Fatihah",
                nameArabic = "الفاتحة",
                versesCount = 7
            ),
            SurahItemReponse(
                id = 2,
                revelationPlace = "madinah",
                revelationOrder = 87,
                nameSimple = "Al-Baqarah",
                nameArabic = "البقرة",
                versesCount = 286
            )
        )
    }

    fun listJuzReponse(): List<JuzItemResponse> {
        return arrayListOf(
            JuzItemResponse(
                id = 1,
                juzNumber = 1,
                firstVerseId = 1,
                lastVerseId = 148,
                verseMapping = hashMapOf(
                    "1" to "1-7",
                    "2" to "1-141"
                ),
                versesCount = 148
            ),
            JuzItemResponse(
                id = 2,
                juzNumber = 2,
                firstVerseId = 149,
                lastVerseId = 259,
                verseMapping = hashMapOf(
                    "2" to "142-252"
                ),
                versesCount = 111
            ),
            JuzItemResponse(
                id = 3,
                juzNumber = 3,
                firstVerseId = 260,
                lastVerseId = 385,
                verseMapping = hashMapOf(
                    "2" to "253-286",
                    "3" to "1-92"
                ),
                versesCount = 126
            )
        )
    }

    fun listAyatReponse(): List<AyatItemResponse> {
        return arrayListOf(
            AyatItemResponse(
                id = 1,
                verseKey = "1:1",
                textUthmani = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ"
            ),
            AyatItemResponse(
                id = 2,
                verseKey = "1:2",
                textUthmani = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ"
            ),
            AyatItemResponse(
                id = 3,
                verseKey = "1:3",
                textUthmani = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ"
            ),
            AyatItemResponse(
                id = 4,
                verseKey = "1:4",
                textUthmani = "مَـٰلِكِ يَوْمِ ٱلدِّينِ"
            ),
            AyatItemResponse(
                id = 5,
                verseKey = "1:5",
                textUthmani = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ"
            ),
            AyatItemResponse(
                id = 6,
                verseKey = "1:6",
                textUthmani = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ"
            ),
            AyatItemResponse(
                id = 7,
                verseKey = "1:7",
                textUthmani = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ"
            ),
        )
    }
}
