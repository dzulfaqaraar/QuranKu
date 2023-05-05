package com.dzulfaqar.quranku.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzulfaqar.quranku.core.data.source.local.entity.JuzEntity
import com.dzulfaqar.quranku.core.data.source.local.entity.JuzSurahEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JuzDao {

    @Query("SELECT * FROM juz")
    fun getAllJuz(): Flow<List<JuzEntity>>

    @Query("DELETE FROM juz")
    suspend fun deleteListJuz()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListJuz(listJuz: List<JuzEntity>)

    @Query("DELETE FROM juz_surah")
    suspend fun deleteListJuzSurah()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListJuzSurah(listJuzSurah: List<JuzSurahEntity>)

    @Query("SELECT * FROM juz_surah WHERE juz_id = :juzId")
    suspend fun getJuzSurah(juzId: Int?): List<JuzSurahEntity>
}
