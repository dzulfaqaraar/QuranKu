package com.dzulfaqar.quranku.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "juz_surah")
data class JuzSurahEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "juz_surah_id")
    var id: Int = 0,
    @ColumnInfo(name = "juz_id")
    var juzId: Int?,
    @ColumnInfo(name = "surah")
    var surah: Int?,
    @ColumnInfo(name = "mapping")
    var mapping: String?
)
