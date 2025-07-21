package com.dzulfaqar.quranku.core.data

import com.dzulfaqar.quranku.core.data.source.local.LocalDataSource
import com.dzulfaqar.quranku.core.data.source.remote.RemoteDataSource
import com.dzulfaqar.quranku.core.data.source.remote.network.ApiResponse
import com.dzulfaqar.quranku.core.domain.model.AyatDomain
import com.dzulfaqar.quranku.core.domain.model.JuzDomain
import com.dzulfaqar.quranku.core.domain.model.SurahDomain
import com.dzulfaqar.quranku.core.util.DataDummy
import com.dzulfaqar.quranku.core.utils.DomainMapper
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CoreRepositoryTest {
    private val local = mock(LocalDataSource::class.java)
    private val remote = mock(RemoteDataSource::class.java)
    private lateinit var coreRepository: FakeCoreRepository

    private val errorMessage = "Something went wrong."

    private val surahDomain = SurahDomain(
        id = 1,
        name = "Al-Fatihah",
        arabic = "الفاتحة",
        totalAyat = 7
    )

    private val juzDomain = JuzDomain(
        number = 1,
        firstAyat = 1,
        lastAyat = 148,
        totalSurat = 2,
        totalAyat = 148
    )

    private val listSurahResponse = DataDummy.listSurahReponse()
    private val listSurahEntity = DomainMapper.mapSurahResponsesToEntities(listSurahResponse)

    private val listJuzResponse = DataDummy.listJuzReponse()
    private val listJuzEntity = DomainMapper.mapJuzResponsesToEntities(listJuzResponse)
    private val listJuzSurahEntity =
        DomainMapper.mapJuzResponsesToEntitiesMapping(listJuzResponse, listJuzEntity)
    private val surahIds = listJuzSurahEntity.map { it.surah ?: 0 }

    private val listAyatResponse = DataDummy.listAyatReponse()
    private val listAyatEntity = DomainMapper.mapAyatResponsesToEntities(listAyatResponse)
    private val ayatEntity = listAyatEntity.first()

    private val surahNames =
        listSurahEntity.associate { (it.surahNumber ?: 0) to (it.surahName ?: "") }
    private val listAyatDomain =
        DomainMapper.mapAyatEntitiesToDomainWithListSurah(surahNames, listAyatEntity)
    private val ayatDomain = listAyatDomain.first()

    private val bookmarkedAyatEntity = DomainMapper.mapAyatResponsesToEntities(listAyatResponse)
    private val bookmarkedIds = bookmarkedAyatEntity.map { it.surah }
    private val bookmarkedAyatDomain =
        DomainMapper.mapAyatEntitiesToDomainWithListSurah(surahNames, bookmarkedAyatEntity)

    @Test
    fun getThemeSetting() = runTest {
        whenever(local.getThemeSetting()).thenReturn(flow {
            emit(true)
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getThemeSetting()
        verify(local, times(1)).getThemeSetting()

        val result = responseFlow.first()
        assertTrue(result)
    }

    @Test
    fun saveThemeSetting() = runTest {
        coreRepository = FakeCoreRepository(local, remote)
        coreRepository.saveThemeSetting(true)

        verify(local, times(1)).saveThemeSetting(true)
    }

    @Test
    fun getAllSurahLoading() = runTest {
        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllSurah()
        val result = responseFlow.first()
        assertTrue(result is Resource.Loading)
    }

    @Test
    fun getAllSurahEmpty() = runTest {
        whenever(local.getAllSurah()).thenReturn(flow {
            emit(arrayListOf())
        })
        whenever(remote.getAllSurah()).thenReturn(flow {
            emit(ApiResponse.Success(arrayListOf()))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllSurah()
        val result = responseFlow.last()

        verify(local, times(2)).getAllSurah()
        verify(remote, times(1)).getAllSurah()
        verify(local, times(1)).insertListSurah(arrayListOf())

        assertTrue(result is Resource.Success)
        assertEquals(0, result.data?.size)
    }

    @Test
    fun getAllSurahSuccess() = runTest {
        whenever(local.getAllSurah()).thenReturn(flow {
            emit(listSurahEntity)
        })
        whenever(remote.getAllSurah()).thenReturn(flow {
            emit(ApiResponse.Success(listSurahResponse))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllSurah()
        val result = responseFlow.last()

        verify(local, times(2)).getAllSurah()
        verify(remote, times(1)).getAllSurah()
        verify(local, times(1)).insertListSurah(listSurahEntity)

        assertTrue(result is Resource.Success)
        assertEquals(listSurahEntity.size, result.data?.size)
        assertEquals(null, result.message)
    }

    @Test
    fun getAllSurahFailed() = runTest {
        whenever(local.getAllSurah()).thenReturn(flow {
            emit(listSurahEntity)
        })
        whenever(remote.getAllSurah()).thenReturn(flow {
            emit(ApiResponse.Error(errorMessage))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllSurah()
        val result = responseFlow.last()

        verify(local, times(1)).getAllSurah()
        verify(remote, times(1)).getAllSurah()

        assertTrue(result is Resource.Error)
        assertEquals(null, result.data)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun getAllJuzLoading() = runTest {
        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllJuz()
        val result = responseFlow.first()
        assertTrue(result is Resource.Loading)
    }

    @Test
    fun getAllJuzEmpty() = runTest {
        whenever(local.getAllJuz()).thenReturn(flow {
            emit(arrayListOf())
        })
        whenever(remote.getAllJuz()).thenReturn(flow {
            emit(ApiResponse.Success(arrayListOf()))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllJuz()
        val result = responseFlow.last()

        verify(local, times(3)).getAllJuz()
        verify(remote, times(1)).getAllJuz()
        verify(local, times(1)).insertListJuz(arrayListOf())

        assertTrue(result is Resource.Success)
        assertEquals(0, result.data?.size)
    }

    @Test
    fun getAllJuzSuccess() = runTest {
        whenever(local.getAllJuz()).thenReturn(flow {
            emit(listJuzEntity)
        })
        whenever(remote.getAllJuz()).thenReturn(flow {
            emit(ApiResponse.Success(listJuzResponse))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllJuz()
        val result = responseFlow.last()

        verify(local, times(3)).getAllJuz()
        verify(remote, times(1)).getAllJuz()
        verify(local, times(1)).insertListJuz(listJuzEntity)

        assertTrue(result is Resource.Success)
        assertEquals(listJuzEntity.size, result.data?.size)
        assertEquals(null, result.message)
    }

    @Test
    fun getAllJuzFailed() = runTest {
        whenever(local.getAllJuz()).thenReturn(flow {
            emit(listJuzEntity)
        })
        whenever(remote.getAllJuz()).thenReturn(flow {
            emit(ApiResponse.Error(errorMessage))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllJuz()
        val result = responseFlow.last()

        verify(local, times(1)).getAllJuz()
        verify(remote, times(1)).getAllJuz()

        assertTrue(result is Resource.Error)
        assertEquals(null, result.data)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun getAllAyatLoading() = runTest {
        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyat()
        val result = responseFlow.first()
        assertTrue(result is Resource.Loading)
    }

    @Test
    fun getAllAyatEmpty() = runTest {
        whenever(local.getAllAyat()).thenReturn(flow {
            emit(arrayListOf())
        })
        whenever(remote.getAllAyat()).thenReturn(flow {
            emit(ApiResponse.Success(arrayListOf()))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyat()
        val result = responseFlow.last()

        verify(local, times(2)).getAllAyat()
        verify(remote, times(1)).getAllAyat()
        verify(local, times(1)).insertListAyat(arrayListOf())

        assertTrue(result is Resource.Success)
        assertEquals(0, result.data?.size)
    }

    @Test
    fun getAllAyatSuccess() = runTest {
        whenever(local.getAllAyat()).thenReturn(flow {
            emit(listAyatEntity)
        })
        whenever(remote.getAllAyat()).thenReturn(flow {
            emit(ApiResponse.Success(listAyatResponse))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyat()
        val result = responseFlow.last()

        verify(local, times(2)).getAllAyat()
        verify(remote, times(1)).getAllAyat()
        verify(local, times(1)).insertListAyat(listAyatEntity)

        assertTrue(result is Resource.Success)
        assertEquals(listAyatEntity.size, result.data?.size)
        assertEquals(null, result.message)
    }

    @Test
    fun getAllAyatFailed() = runTest {
        whenever(local.getAllAyat()).thenReturn(flow {
            emit(listAyatEntity)
        })
        whenever(remote.getAllAyat()).thenReturn(flow {
            emit(ApiResponse.Error(errorMessage))
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyat()
        val result = responseFlow.last()

        verify(local, times(1)).getAllAyat()
        verify(remote, times(1)).getAllAyat()

        assertTrue(result is Resource.Error)
        assertEquals(null, result.data)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun getAllAyatBySurahEmpty() = runTest {
        whenever(local.getAllAyatBySurah(surahDomain.id)).thenReturn(flow {
            emit(arrayListOf())
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyatBySurah(surahDomain)
        val result = responseFlow.last()

        verify(local, times(1)).getAllAyatBySurah(surahDomain.id)

        assertEquals(0, result.size)
    }

    @Test
    fun getAllAyatBySurahSuccess() = runTest {
        whenever(local.getAllAyatBySurah(surahDomain.id)).thenReturn(flow {
            emit(listAyatEntity)
        })

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyatBySurah(surahDomain)
        val result = responseFlow.last()

        verify(local, times(1)).getAllAyatBySurah(surahDomain.id)

        assertEquals(listAyatEntity.size, result.size)
    }

    @Test
    fun getAllAyatByJuzEmpty() = runTest {
        whenever(local.getJuzSurah(juzDomain.number)).thenReturn(arrayListOf())
        whenever(local.getSurahByIds(arrayListOf())).thenReturn(arrayListOf())
        whenever(local.getAllAyatByJuz(juzDomain.firstAyat, juzDomain.lastAyat))
            .thenReturn(arrayListOf())

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyatByJuz(juzDomain)
        val result = responseFlow.last()

        verify(local, times(1)).getJuzSurah(juzDomain.number)
        verify(local, times(1)).getSurahByIds(arrayListOf())
        verify(local, times(1)).getAllAyatByJuz(juzDomain.firstAyat, juzDomain.lastAyat)

        assertEquals(0, result.size)
    }

    @Test
    fun getAllAyatByJuzSuccess() = runTest {
        whenever(local.getJuzSurah(juzDomain.number)).thenReturn(listJuzSurahEntity)
        whenever(local.getSurahByIds(surahIds)).thenReturn(listSurahEntity)
        whenever(local.getAllAyatByJuz(juzDomain.firstAyat, juzDomain.lastAyat))
            .thenReturn(listAyatEntity)

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllAyatByJuz(juzDomain)
        val result = responseFlow.last()

        verify(local, times(1)).getJuzSurah(juzDomain.number)
        verify(local, times(1)).getSurahByIds(surahIds)
        verify(local, times(1)).getAllAyatByJuz(juzDomain.firstAyat, juzDomain.lastAyat)

        assertEquals(listAyatDomain, result)
    }

    @Test
    fun updateBookmark() = runTest {
        coreRepository = FakeCoreRepository(local, remote)
        coreRepository.updateBookmark(ayatDomain, true)

        verify(local, times(1)).updateBookmark(ayatEntity, true)
    }

    @Test
    fun getAllBookmarkEmpty() = runTest {
        whenever(local.getAllBookmark()).thenReturn(arrayListOf())
        whenever(local.getSurahByIds(arrayListOf())).thenReturn(arrayListOf())

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllBookmark()
        val result = responseFlow.first()

        verify(local, times(1)).getAllBookmark()

        assertEquals(arrayListOf<AyatDomain>(), result)
    }

    @Test
    fun getAllBookmarkSuccess() = runTest {
        whenever(local.getAllBookmark()).thenReturn(bookmarkedAyatEntity)
        whenever(local.getSurahByIds(bookmarkedIds)).thenReturn(listSurahEntity)

        coreRepository = FakeCoreRepository(local, remote)

        val responseFlow = coreRepository.getAllBookmark()
        val result = responseFlow.first()

        verify(local, times(1)).getAllBookmark()

        assertEquals(bookmarkedAyatDomain, result)
    }
}
