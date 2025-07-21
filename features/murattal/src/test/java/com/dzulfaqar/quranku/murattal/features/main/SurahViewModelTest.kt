package com.dzulfaqar.quranku.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.SurahDomain
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.features.main.surah.SurahViewModel
import com.dzulfaqar.quranku.util.DataDummy
import com.dzulfaqar.quranku.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.core.utils.getValueForTest
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SurahViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: QuranUseCase

    private lateinit var viewModel: SurahViewModel

    private val errorMessage = "Something went wrong."

    private val listSurah: List<SurahDomain> = DataDummy.listSurahDomain()

    @Test
    fun getListSurahLoading() {
        whenever(useCase.getAllSurah()).thenReturn(flow { emit(Resource.Loading()) })

        viewModel = SurahViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listSurah.getValueForTest()

        verify(useCase, times(1)).getAllSurah()

        assertThat(result, instanceOf(Resource.Loading::class.java))
        assertEquals(null, result?.data)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListSurahEmpty() {
        whenever(useCase.getAllSurah()).thenReturn(flow { emit(Resource.Success(arrayListOf())) })

        viewModel = SurahViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listSurah.getValueForTest()

        verify(useCase, times(1)).getAllSurah()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(0, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListSurahSuccess() {
        whenever(useCase.getAllSurah()).thenReturn(flow {
            emit(Resource.Success(listSurah))
        })

        viewModel = SurahViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listSurah.getValueForTest()

        verify(useCase, times(1)).getAllSurah()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(listSurah.size, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListSurahFailed() {
        whenever(useCase.getAllSurah()).thenReturn(flow { emit(Resource.Error(errorMessage)) })

        viewModel = SurahViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listSurah.getValueForTest()

        verify(useCase, times(1)).getAllSurah()

        assertThat(result, instanceOf(Resource.Error::class.java))
        assertEquals(null, result?.data)
        assertEquals(errorMessage, result?.message)
    }
}