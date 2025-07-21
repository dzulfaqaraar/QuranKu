package com.dzulfaqar.quranku.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dzulfaqar.quranku.core.data.source.local.entity.AyatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AyatDao {

    @Query("SELECT * FROM ayat")
    fun getAllAyat(): Flow<List<AyatEntity>>

    @Query("SELECT * FROM ayat WHERE surah = :surahId")
    fun getAllAyatBySurah(surahId: Int?): Flow<List<AyatEntity>>

    @Query("SELECT * FROM ayat WHERE position >= :firstAyat AND position <= :lastAyat")
    suspend fun getAllAyatByJuz(firstAyat: Int?, lastAyat: Int?): List<AyatEntity>

    @Query("DELETE FROM ayat")
    suspend fun deleteListAyat()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListAyat(listAyat: List<AyatEntity>)

    @Update
    suspend fun updateBookmark(ayat: AyatEntity)

    @Query("SELECT * FROM ayat WHERE is_bookmark = 1")
    fun getAllBookmark(): List<AyatEntity>
}
