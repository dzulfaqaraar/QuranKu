package com.dzulfaqar.quranku.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dzulfaqar.quranku.core.data.source.local.entity.*

@Database(
    entities = [
        SurahEntity::class,
        JuzEntity::class,
        JuzSurahEntity::class,
        AyatEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun juzDao(): JuzDao
    abstract fun ayatDao(): AyatDao
}
