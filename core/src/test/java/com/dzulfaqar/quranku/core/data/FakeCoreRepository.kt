package com.dzulfaqar.quranku.core.data

import com.dzulfaqar.quranku.core.data.source.local.LocalDataSource
import com.dzulfaqar.quranku.core.data.source.remote.RemoteDataSource
import com.dzulfaqar.quranku.core.data.source.remote.network.ApiResponse
import com.dzulfaqar.quranku.core.data.source.remote.response.*
import com.dzulfaqar.quranku.core.domain.model.*
import com.dzulfaqar.quranku.core.domain.repository.ICoreRepository
import com.dzulfaqar.quranku.core.utils.Constant
import com.dzulfaqar.quranku.core.utils.DomainMapper
import kotlinx.coroutines.flow.*

class FakeCoreRepository constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ICoreRepository {

    override fun getThemeSetting() = localDataSource.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        localDataSource.saveThemeSetting(isDarkModeActive)
    }

    override fun getAllSurah(): Flow<Resource<List<SurahDomain>>> =
        object : NetworkBoundResource<List<SurahDomain>, List<SurahItemReponse>>() {
            override fun loadFromDB(): Flow<List<SurahDomain>> =
                localDataSource.getAllSurah().map {
                    DomainMapper.mapSurahEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<SurahDomain>?): Boolean =
                data?.size != Constant.TOTAL_SURAH

            override suspend fun createCall(): Flow<ApiResponse<List<SurahItemReponse>>> =
                remoteDataSource.getAllSurah()

            override suspend fun saveCallResult(data: List<SurahItemReponse>) {
                val listSurah = DomainMapper.mapSurahResponsesToEntities(data)
                localDataSource.insertListSurah(listSurah)
            }
        }.asFlow()

    override fun getAllJuz(): Flow<Resource<List<JuzDomain>>> =
        object : NetworkBoundResource<List<JuzDomain>, List<JuzItemResponse>>() {
            override fun loadFromDB(): Flow<List<JuzDomain>> =
                localDataSource.getAllJuz().map {
                    DomainMapper.mapJuzEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<JuzDomain>?): Boolean =
                data?.size != Constant.TOTAL_JUZ

            override suspend fun createCall(): Flow<ApiResponse<List<JuzItemResponse>>> =
                remoteDataSource.getAllJuz()

            override suspend fun saveCallResult(data: List<JuzItemResponse>) {
                val listJuz = DomainMapper.mapJuzResponsesToEntities(data)
                localDataSource.insertListJuz(listJuz)

                val savedJuz = localDataSource.getAllJuz().first()
                val listJuzSurah = DomainMapper.mapJuzResponsesToEntitiesMapping(data, savedJuz)
                localDataSource.insertListJuzSurah(listJuzSurah)
            }
        }.asFlow()

    override fun getAllAyat(): Flow<Resource<List<AyatDomain>>> =
        object : NetworkBoundResource<List<AyatDomain>, List<AyatItemResponse>>() {
            override fun loadFromDB(): Flow<List<AyatDomain>> =
                localDataSource.getAllAyat().map {
                    DomainMapper.mapAyatEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<AyatDomain>?): Boolean =
                data?.size != Constant.TOTAL_AYAT

            override suspend fun createCall(): Flow<ApiResponse<List<AyatItemResponse>>> =
                remoteDataSource.getAllAyat()

            override suspend fun saveCallResult(data: List<AyatItemResponse>) {
                val listAyat = DomainMapper.mapAyatResponsesToEntities(data)
                localDataSource.insertListAyat(listAyat)
            }
        }.asFlow()

    override fun getAllAyatBySurah(surah: SurahDomain?): Flow<List<AyatDomain>> =
        localDataSource.getAllAyatBySurah(surah?.id).map {
            DomainMapper.mapAyatEntitiesToDomainBySurah(surah?.name, it)
        }

    override fun getAllAyatByJuz(juz: JuzDomain?): Flow<List<AyatDomain>> {
        return flow {
            val listJuzSurah = localDataSource.getJuzSurah(juz?.number)
                .map { it.surah ?: 0 }
            val listSurahName = localDataSource.getSurahByIds(listJuzSurah)
                .associate { (it.surahNumber ?: 0) to (it.surahName ?: "") }

            val result = localDataSource.getAllAyatByJuz(juz?.firstAyat, juz?.lastAyat)
            emit(DomainMapper.mapAyatEntitiesToDomainWithListSurah(listSurahName, result))
        }
    }

    override suspend fun updateBookmark(ayat: AyatDomain, isBookmark: Boolean) {
        val ayatEntity = DomainMapper.mapAyatDomainToEntity(ayat)
        localDataSource.updateBookmark(ayatEntity, isBookmark)
    }

    override suspend fun getAllBookmark(): Flow<List<AyatDomain>> {
        return flow {
            val result = localDataSource.getAllBookmark()
            val listSurahId = result.map { it.surah }
            val listSurahName = localDataSource.getSurahByIds(listSurahId)
                .associate { (it.surahNumber ?: 0) to (it.surahName ?: "") }

            emit(DomainMapper.mapAyatEntitiesToDomainWithListSurah(listSurahName, result))
        }
    }

    override fun getAllReciter(): Flow<Resource<List<ReciterDomain>>> {
        return flow {
            emit(Resource.Loading())
            remoteDataSource.getAllReciter().collect { response ->
                when (response) {
                    is ApiResponse.Success -> emit(Resource.Success(
                        DomainMapper.mapReciterResponsesToDomain(response.data)
                    ))
                    is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
            }
        }
    }

    override fun getLatestRecitation() = localDataSource.getLatestRecitation().map {
        DomainMapper.mapRecitationPreferenceToDomain(it)
    }

    override suspend fun setLatestRecitation(latest: ReciterDomain) {
        localDataSource.setLatestRecitation(DomainMapper.mapReciterModelToPreference(latest))
    }

    override fun getRecitationByChapter(id: Int, surah: Int): Flow<Resource<List<AudioDomain>>> {
        return flow {
            emit(Resource.Loading())
            remoteDataSource.getRecitationByChapter(id, surah).collect { response ->
                when (response) {
                    is ApiResponse.Success -> emit(Resource.Success(
                        DomainMapper.mapAudioResponsesToDomain(response.data)
                    ))
                    is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
            }
        }
    }
}
