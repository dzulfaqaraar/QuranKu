package com.dzulfaqar.quranku.bookmark.util

import com.dzulfaqar.quranku.core.domain.model.AyatDomain

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
}
