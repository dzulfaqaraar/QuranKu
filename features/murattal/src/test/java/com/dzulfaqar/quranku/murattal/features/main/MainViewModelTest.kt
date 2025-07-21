package com.dzulfaqar.quranku.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import com.dzulfaqar.quranku.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.core.utils.getValueForTest
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: SettingUseCase

    private lateinit var viewModel: MainViewModel

    @Test
    fun getThemeSetting() {
        whenever(useCase.getThemeSetting()).thenReturn(flow { emit(true) })

        viewModel = MainViewModel(useCase)

        val result = viewModel.themeSetting.getValueForTest()

        verify(useCase, times(1)).getThemeSetting()

        assertNotNull(result)
        assertEquals(true, result)
    }
}
