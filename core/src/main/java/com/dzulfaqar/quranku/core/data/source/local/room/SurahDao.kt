package com.dzulfaqar.quranku.core.data.source.local.room

import androidx.room.*
import com.dzulfaqar.quranku.core.data.source.local.entity.SurahEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {

    @Query("SELECT * FROM surah")
    fun getAllSurah(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surah WHERE surah_number IN(:numbers)")
    suspend fun getSurahByIds(numbers: List<Int>): List<SurahEntity>

    @Query("DELETE FROM surah")
    suspend fun deleteListSurah()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSurah(listSurah: List<SurahEntity>)
}
