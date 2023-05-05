package com.dzulfaqar.quranku.core.data.source.remote

import com.dzulfaqar.quranku.core.data.source.remote.network.ApiResponse
import com.dzulfaqar.quranku.core.data.source.remote.network.ApiService
import com.dzulfaqar.quranku.core.data.source.remote.response.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllSurah(): Flow<ApiResponse<List<SurahItemReponse>>> {
        return flow {
            val response = apiService.getAllSurah()
            val dataArray = response.chapters
            if (dataArray != null && dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.chapters))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Timber.d(e.toString())
        }
    }

    suspend fun getAllJuz(): Flow<ApiResponse<List<JuzItemResponse>>> {
        return flow {
            val response = apiService.getAllJuz()
            val dataArray = response.juzs
            if (dataArray != null && dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.juzs))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Timber.d(e.toString())
        }
    }

    suspend fun getAllAyat(): Flow<ApiResponse<List<AyatItemResponse>>> {
        return flow {
            val response = apiService.getAllAyat()
            val dataArray = response.verses
            if (dataArray != null && dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.verses))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Timber.d(e.toString())
        }
    }

    suspend fun getAllReciter(): Flow<ApiResponse<List<RecitationsItemResponse>>> {
        return flow {
            val response = apiService.getAllReciter()
            val dataArray = response.recitations
            if (dataArray != null && dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.recitations))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Timber.d(e.toString())
        }
    }

    suspend fun getRecitationByChapter(
        id: Int,
        surah: Int
    ): Flow<ApiResponse<List<AudioItemResponse>>> {
        return flow {
            val response = apiService.getRecitationByChapter(id, surah)
            val dataArray = response.audioFiles
            if (dataArray != null && dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.audioFiles))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Timber.d(e.toString())
        }
    }
}
