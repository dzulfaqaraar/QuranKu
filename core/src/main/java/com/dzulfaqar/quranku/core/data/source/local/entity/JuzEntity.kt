package com.dzulfaqar.quranku.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "juz")
data class JuzEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "juz_id")
    var id: Int = 0,
    @ColumnInfo(name = "juz_number")
    var juzNumber: Int?,
    @ColumnInfo(name = "first_ayat")
    var firstAyat: Int?,
    @ColumnInfo(name = "last_ayat")
    var lastAyat: Int?,
    @ColumnInfo(name = "total_surah")
    var totalSurah: Int?,
    @ColumnInfo(name = "total_ayat")
    var totalAyat: Int?
)
