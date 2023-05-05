package com.dzulfaqar.quranku.features.quran

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.AudioDomain
import com.dzulfaqar.quranku.core.domain.model.AyatDomain
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.model.AyatModel
import com.dzulfaqar.quranku.model.JuzModel
import com.dzulfaqar.quranku.model.ReciterModel
import com.dzulfaqar.quranku.model.SurahModel
import com.dzulfaqar.quranku.util.DataDummy
import com.dzulfaqar.quranku.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.util.getValueForTest
import com.dzulfaqar.quranku.utils.AppMapper
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuranViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: QuranUseCase

    private lateinit var viewModel: QuranViewModel

    private val errorMessage = "Something went wrong."

    private val listAyat: List<AyatDomain> = DataDummy.listAyatDomain()

    private val reciterModel = ReciterModel(1, "AbdulBaset AbdulSamad", "Murattal")

    private val reciterDomain = AppMapper.mapReciterModelToDomain(reciterModel)

    private val surahModel: SurahModel = SurahModel(1, "Al-Fatihah", "الفاتحة", 7)

    private val juzModel: JuzModel = JuzModel(1, 1, 148, 2, 148)

    private val ayatModel: AyatModel =
        AyatModel(1, 1, 1, "Al-Fatihah", 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ", false)

    private val listAudio: List<AudioDomain> = DataDummy.listAudioDomain()

    private fun mockInit() {
        whenever(useCase.getLatestRecitation()).thenReturn(flow { emit(reciterDomain) })
        whenever(useCase.getAllAyat()).thenReturn(flow { emit(Resource.Success(listAyat)) })
    }

    @Test
    fun getListAyatLoading() {
        whenever(useCase.getLatestRecitation()).thenReturn(flow { emit(reciterDomain) })
        whenever(useCase.getAllAyat()).thenReturn(flow { emit(Resource.Loading()) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listAyatState.getValueForTest()

        verify(useCase, times(1)).getAllAyat()

        assertThat(result, instanceOf(Resource.Loading::class.java))
        assertEquals(null, result?.data)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListAyatEmpty() {
        whenever(useCase.getLatestRecitation()).thenReturn(flow { emit(reciterDomain) })
        whenever(useCase.getAllAyat()).thenReturn(flow { emit(Resource.Success(arrayListOf())) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listAyatState.getValueForTest()

        verify(useCase, times(1)).getAllAyat()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(0, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListAyatSuccess() {
        mockInit()

        viewModel = QuranViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listAyatState.getValueForTest()

        verify(useCase, times(1)).getAllAyat()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(listAyat.size, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListAyatFailed() {
        whenever(useCase.getLatestRecitation()).thenReturn(flow { emit(reciterDomain) })
        whenever(useCase.getAllAyat()).thenReturn(flow { emit(Resource.Error(errorMessage)) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listAyatState.getValueForTest()

        verify(useCase, times(1)).getAllAyat()

        assertThat(result, instanceOf(Resource.Error::class.java))
        assertEquals(null, result?.data)
        assertEquals(errorMessage, result?.message)
    }

    @Test
    fun getLatestRecitation() {
        whenever(useCase.getAllAyat()).thenReturn(flow { emit(Resource.Loading()) })
        whenever(useCase.getLatestRecitation()).thenReturn(flow { emit(reciterDomain) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)

        val result = viewModel.latestRecitation.getValueForTest()

        verify(useCase, times(1)).getLatestRecitation()

        val expected = AppMapper.mapReciterDomainToPresentation(reciterDomain)
        assertEquals(expected, result)
    }

    @Test
    fun getAllAyatBySurahEmpty() {
        mockInit()

        val surahDomain = AppMapper.mapSurahModelToDomain(surahModel)
        whenever(useCase.getAllAyatBySurah(surahDomain)).thenReturn(flow { emit(arrayListOf()) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getAllAyatBySurah(surahModel)

        val result = viewModel.listAyatData.getValueForTest()

        verify(useCase, times(1)).getAllAyatBySurah(surahDomain)

        assertEquals(0, result?.size)
    }

    @Test
    fun getAllAyatBySurahSuccess() {
        mockInit()

        val surahDomain = AppMapper.mapSurahModelToDomain(surahModel)
        whenever(useCase.getAllAyatBySurah(surahDomain)).thenReturn(flow { emit(listAyat) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getAllAyatBySurah(surahModel)

        val result = viewModel.listAyatData.getValueForTest()

        verify(useCase, times(1)).getAllAyatBySurah(surahDomain)

        assertEquals(listAyat.size, result?.size)
    }

    @Test
    fun getAllAyatByJuzEmpty() {
        mockInit()

        val juzDomain = AppMapper.mapJuzModelToDomain(juzModel)
        whenever(useCase.getAllAyatByJuz(juzDomain)).thenReturn(flow { emit(arrayListOf()) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getAllAyatByJuz(juzModel)

        val result = viewModel.listAyatData.getValueForTest()

        verify(useCase, times(1)).getAllAyatByJuz(juzDomain)

        assertEquals(0, result?.size)
    }

    @Test
    fun getAllAyatByJuzSuccess() {
        mockInit()

        val juzDomain = AppMapper.mapJuzModelToDomain(juzModel)
        whenever(useCase.getAllAyatByJuz(juzDomain)).thenReturn(flow { emit(listAyat) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getAllAyatByJuz(juzModel)

        val result = viewModel.listAyatData.getValueForTest()

        verify(useCase, times(1)).getAllAyatByJuz(juzDomain)

        assertEquals(listAyat.size, result?.size)
    }

    @Test
    fun bookmarkAyat() = runBlockingTest {
        mockInit()

        val ayatDomain = AppMapper.mapAyatModelToDomain(ayatModel)

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.bookmarkAyat(ayatModel, true)

        verify(useCase, times(1)).updateBookmark(ayatDomain, true)
    }

    @Test
    fun setLatestRecitation() = runBlockingTest {
        mockInit()

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.selectedReciter = reciterModel
        viewModel.setLatestRecitation()

        verify(useCase, times(1)).setLatestRecitation(reciterDomain)
    }

    @Test
    fun getRecitationByChapterLoading() {
        mockInit()

        whenever(
            useCase.getRecitationByChapter(
                anyInt(),
                anyInt()
            )
        ).thenReturn(flow { emit(Resource.Loading()) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getRecitationByChapter(1, 1)

        val result = viewModel.listAudioFiles.getValueForTest()

        verify(useCase, times(1)).getRecitationByChapter(1, 1)

        assertThat(result, instanceOf(Resource.Loading::class.java))
        assertEquals(null, result?.data)
        assertEquals(null, result?.message)
    }

    @Test
    fun getRecitationByChapterEmpty() {
        mockInit()

        whenever(
            useCase.getRecitationByChapter(
                anyInt(),
                anyInt()
            )
        ).thenReturn(flow { emit(Resource.Success(arrayListOf())) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getRecitationByChapter(1, 1)

        val result = viewModel.listAudioFiles.getValueForTest()

        verify(useCase, times(1)).getRecitationByChapter(1, 1)

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(0, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getRecitationByChapterSuccess() {
        mockInit()

        whenever(
            useCase.getRecitationByChapter(
                anyInt(),
                anyInt()
            )
        ).thenReturn(flow { emit(Resource.Success(listAudio)) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getRecitationByChapter(1, 1)

        val result = viewModel.listAudioFiles.getValueForTest()

        verify(useCase, times(1)).getRecitationByChapter(1, 1)

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(listAudio.size, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getRecitationByChapterFailed() {
        mockInit()

        whenever(
            useCase.getRecitationByChapter(
                anyInt(),
                anyInt()
            )
        ).thenReturn(flow { emit(Resource.Error(errorMessage)) })

        viewModel = QuranViewModel(coroutineContextProvider, useCase)
        viewModel.getRecitationByChapter(1, 1)

        val result = viewModel.listAudioFiles.getValueForTest()

        verify(useCase, times(1)).getRecitationByChapter(1, 1)

        assertThat(result, instanceOf(Resource.Error::class.java))
        assertEquals(null, result?.data)
        assertEquals(errorMessage, result?.message)
    }
}
