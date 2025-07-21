package com.dzulfaqar.quranku.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.SettingUseCase
import com.dzulfaqar.quranku.setting.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.core.utils.getValueForTest
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
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
class SettingViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: SettingUseCase

    private lateinit var viewModel: SettingViewModel

    @Test
    fun saveThemeSetting() = runTest {
        whenever(useCase.getThemeSetting()).thenReturn(flow { emit(true) })

        viewModel = SettingViewModel(coroutineContextProvider, useCase)
        viewModel.saveThemeSetting(true)

        val result = viewModel.themeSetting.getValueForTest()

        verify(useCase, times(1)).getThemeSetting()
        verify(useCase, times(1)).saveThemeSetting(true)

        assertNotNull(result)
        assertEquals(true, result)
    }
}
