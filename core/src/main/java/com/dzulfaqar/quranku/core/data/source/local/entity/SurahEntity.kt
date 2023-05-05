package com.dzulfaqar.quranku.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surah")
data class SurahEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "surah_id")
    var id: Int = 0,
    @ColumnInfo(name = "surah_number")
    var surahNumber: Int?,
    @ColumnInfo(name = "revelation_place")
    var revelationPlace: String?,
    @ColumnInfo(name = "revelation_order")
    var revelationOrder: Int?,
    @ColumnInfo(name = "surah_name")
    var surahName: String?,
    @ColumnInfo(name = "arabic_name")
    var arabicName: String?,
    @ColumnInfo(name = "verses_count")
    var versesCount: Int?
)
