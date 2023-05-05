package com.dzulfaqar.quranku.murattal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.ReciterDomain
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.murattal.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.murattal.util.getValueForTest
import com.nhaarman.mockitokotlin2.whenever
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
class RecitationViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: QuranUseCase

    private lateinit var viewModel: RecitationViewModel

    private val errorMessage = "Something went wrong."

    private val listRecitation: List<ReciterDomain> = arrayListOf(
        ReciterDomain(1, "AbdulBaset AbdulSamad", "Murattal"),
        ReciterDomain(2, "AbdulBaset AbdulSamad", "Mujawwad"),
        ReciterDomain(3, "Abdur-Rahman as-Sudais", null),
        ReciterDomain(4, "Abu Bakr al-Shatri", null),
        ReciterDomain(5, "Hani ar-Rifai", null),
    )

    @Test
    fun getListReciterLoading() {
        whenever(useCase.getAllReciter()).thenReturn(flow { emit(Resource.Loading()) })

        viewModel = RecitationViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listReciter.getValueForTest()

        verify(useCase, times(1)).getAllReciter()

        assertThat(result, instanceOf(Resource.Loading::class.java))
        assertEquals(null, result?.data)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListReciterEmpty() {
        whenever(useCase.getAllReciter()).thenReturn(flow { emit(Resource.Success(arrayListOf())) })

        viewModel = RecitationViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listReciter.getValueForTest()

        verify(useCase, times(1)).getAllReciter()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(0, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListReciterSuccess() {
        whenever(useCase.getAllReciter()).thenReturn(flow {
            emit(Resource.Success(listRecitation))
        })

        viewModel = RecitationViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listReciter.getValueForTest()

        verify(useCase, times(1)).getAllReciter()

        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(listRecitation.size, result?.data?.size)
        assertEquals(null, result?.message)
    }

    @Test
    fun getListReciterFailed() {
        whenever(useCase.getAllReciter()).thenReturn(flow { emit(Resource.Error(errorMessage)) })

        viewModel = RecitationViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listReciter.getValueForTest()

        verify(useCase, times(1)).getAllReciter()

        assertThat(result, instanceOf(Resource.Error::class.java))
        assertEquals(null, result?.data)
        assertEquals(errorMessage, result?.message)
    }
}
