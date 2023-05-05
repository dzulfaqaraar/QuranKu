package com.dzulfaqar.quranku.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ayat")
data class AyatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ayat_id")
    var id: Int = 0,
    @ColumnInfo(name = "position")
    var position: Int,
    @ColumnInfo(name = "surah")
    var surah: Int,
    @ColumnInfo(name = "ayat")
    var ayat: Int,
    @ColumnInfo(name = "arabic")
    var arabic: String,
    @ColumnInfo(name = "is_bookmark")
    var isBookmark: Boolean = false
)
