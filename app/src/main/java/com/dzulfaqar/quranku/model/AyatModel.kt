package com.dzulfaqar.quranku.model

data class AyatModel(
    val id: Int,
    val surahNumber: Int,
    val position: Int,
    val surahName: String,
    val ayatNumber: Int,
    val arabic: String,
    val isBookmark: Boolean
)
