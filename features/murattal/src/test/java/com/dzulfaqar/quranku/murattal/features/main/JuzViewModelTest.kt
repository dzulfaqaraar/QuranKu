package com.dzulfaqar.quranku.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.JuzDomain
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.features.main.juz.JuzViewModel
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
class JuzViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: QuranUseCase

    private lateinit var viewModel: JuzViewModel

    private val errorMessage = "Something went wrong."

    private val listJuz: List<JuzDomain> = DataDummy.listJuzDomain()

    @Test
    fun getListJuzLoading() {
        whenever(useCase.getAllJuz()).thenReturn(flow { emit(Resource.Loading()) })

        viewModel = JuzViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listJuz.getValueForTest()

        verify(useCase, times(1)).getAllJuz()

        assertThat(result, instanceOf(Resource.Loading::class.java))
        assertEquals(null, result?.data)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListJuzEmpty() {
        whenever(useCase.getAllJuz()).thenReturn(flow { emit(Resource.Success(arrayListOf())) })

        viewModel = JuzViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listJuz.getValueForTest()

        verify(useCase, times(1)).getAllJuz()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(0, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListJuzSuccess() {
        whenever(useCase.getAllJuz()).thenReturn(flow { emit(Resource.Success(listJuz)) })

        viewModel = JuzViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listJuz.getValueForTest()

        verify(useCase, times(1)).getAllJuz()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(listJuz.size, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListJuzFailed() {
        whenever(useCase.getAllJuz()).thenReturn(flow { emit(Resource.Error(errorMessage)) })

        viewModel = JuzViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listJuz.getValueForTest()

        verify(useCase, times(1)).getAllJuz()

        assertThat(result, instanceOf(Resource.Error::class.java))
        assertEquals(null, result?.data)
        assertEquals(errorMessage, result?.message)
    }
}
