package com.dzulfaqar.quranku.core.data.source.remote.network

import com.dzulfaqar.quranku.core.data.source.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v4/chapters")
    suspend fun getAllSurah(): SurahResponse

    @GET("api/v4/juzs")
    suspend fun getAllJuz(): JuzResponse

    @GET("api/v4/quran/verses/uthmani")
    suspend fun getAllAyat(): AyatResponse

    @GET("api/v4/resources/recitations")
    suspend fun getAllReciter(): ReciterResponse

    @GET("api/v4/recitations/{recitation_id}/by_chapter/{chapter_number}")
    suspend fun getRecitationByChapter(
        @Path("recitation_id") id: Int,
        @Path("chapter_number") surah: Int
    ): AudioResponse
}
