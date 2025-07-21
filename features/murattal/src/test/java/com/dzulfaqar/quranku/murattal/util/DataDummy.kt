package com.dzulfaqar.quranku.util

import com.dzulfaqar.quranku.core.domain.model.AudioDomain
import com.dzulfaqar.quranku.core.domain.model.AyatDomain
import com.dzulfaqar.quranku.core.domain.model.JuzDomain
import com.dzulfaqar.quranku.core.domain.model.SurahDomain

object DataDummy {
    fun listAyatDomain(): List<AyatDomain> {
        return arrayListOf(
            AyatDomain(1, 1, 1, "Al-Fatihah", 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ", false),
            AyatDomain(2, 1, 2, "Al-Fatihah", 2, "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ", false),
            AyatDomain(3, 1, 3, "Al-Fatihah", 3, "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ", false),
            AyatDomain(4, 1, 4, "Al-Fatihah", 4, "مَـٰلِكِ يَوْمِ ٱلدِّينِ", false),
            AyatDomain(5, 1, 5, "Al-Fatihah", 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", false),
            AyatDomain(6, 1, 6, "Al-Fatihah", 6, "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ", false),
            AyatDomain(7, 1, 7, "Al-Fatihah", 7, "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ", false)
        )
    }
    fun listSurahDomain(): List<SurahDomain> {
        return arrayListOf(
            SurahDomain(1, "Al-Fatihah", "الفاتحة", 7),
            SurahDomain(2, "Al-Baqarah", "البقرة", 286),
            SurahDomain(3, "Ali 'Imran", "آل عمران", 200),
            SurahDomain(4, "An-Nisa", "النساء", 176),
            SurahDomain(5, "Al-Ma'idah", "المائدة", 120),
            SurahDomain(6, "Al-An'am", "الأنعام", 165),
        )
    }
    fun listJuzDomain(): List<JuzDomain> {
        return arrayListOf(
            JuzDomain(1, 1, 148, 2, 148),
            JuzDomain(2, 149, 259, 1, 111),
            JuzDomain(3, 260, 385, 2, 126),
        )
    }
    fun listAudioDomain(): List<AudioDomain> {
        return arrayListOf(
            AudioDomain("1:1", "Sudais/mp3/001001.mp3"),
            AudioDomain("1:2", "Sudais/mp3/001002.mp3"),
            AudioDomain("1:3", "Sudais/mp3/001003.mp3"),
            AudioDomain("1:4", "Sudais/mp3/001004.mp3"),
            AudioDomain("1:5", "Sudais/mp3/001005.mp3"),
            AudioDomain("1:6", "Sudais/mp3/001006.mp3"),
            AudioDomain("1:7", "Sudais/mp3/001007.mp3"),
        )
    }
}
