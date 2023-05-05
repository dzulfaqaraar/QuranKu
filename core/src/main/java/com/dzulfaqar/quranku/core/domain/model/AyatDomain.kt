package com.dzulfaqar.quranku.core.domain.model

data class AyatDomain(
    val id: Int,
    val surahNumber: Int,
    val position: Int,
    val surahName: String,
    val ayatNumber: Int,
    val arabic: String,
    val isBookmark: Boolean
)
