package com.dzulfaqar.quranku.bookmark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dzulfaqar.quranku.bookmark.util.DataDummy
import com.dzulfaqar.quranku.bookmark.util.MainCoroutineScopeRule
import com.dzulfaqar.quranku.core.utils.getValueForTest
import com.dzulfaqar.quranku.core.coroutine.TestCoroutineContextProvider
import com.dzulfaqar.quranku.core.domain.usecase.QuranUseCase
import com.dzulfaqar.quranku.model.AyatModel
import com.dzulfaqar.quranku.utils.mapToDomain
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookmarkViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var coroutineContextProvider = TestCoroutineContextProvider()

    @Mock
    private lateinit var useCase: QuranUseCase

    private lateinit var viewModel: BookmarkViewModel

    private val listAyatDomain = DataDummy.listAyatDomain()
    private val ayatModel = AyatModel(1, 1, 1, "Al-Fatihah", 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ", false)
    private val ayatDomain = ayatModel.mapToDomain()

    @Test
    fun getListBookmarkEmpty() = runTest {
        whenever(useCase.getAllBookmark()).thenReturn(flow { emit(arrayListOf()) })

        viewModel = BookmarkViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listBookmark.getValueForTest()

        verify(useCase, times(1)).getAllBookmark()

        assertNotNull(result)
        assertEquals(0, result?.size)
    }

    @Test
    fun getListBookmarkSuccess() = runTest {
        whenever(useCase.getAllBookmark()).thenReturn(flow { emit(listAyatDomain) })

        viewModel = BookmarkViewModel(coroutineContextProvider, useCase)

        val result = viewModel.listBookmark.getValueForTest()

        verify(useCase, times(1)).getAllBookmark()

        assertNotNull(result)
        assertEquals(listAyatDomain.size, result?.size)
    }

    @Test
    fun bookmarkAyat() = runTest {
        whenever(useCase.getAllBookmark()).thenReturn(flow { emit(listAyatDomain) })

        viewModel = BookmarkViewModel(coroutineContextProvider, useCase)
        viewModel.bookmarkAyat(ayatModel, true, askCanceling = false)

        val deletedData = viewModel.deletedData.value?.peekContent()
        assertNull(deletedData)

        verify(useCase, times(1)).updateBookmark(ayatDomain, true)
        verify(useCase, times(2)).getAllBookmark()
    }

    @Test
    fun bookmarkAyatWithCancel() = runTest {
        whenever(useCase.getAllBookmark()).thenReturn(flow { emit(listAyatDomain) })

        viewModel = BookmarkViewModel(coroutineContextProvider, useCase)
        viewModel.bookmarkAyat(ayatModel, true)

        val deletedData = viewModel.deletedData.value?.peekContent()
        assertNotNull(deletedData)
        assertEquals(ayatModel, deletedData)

        verify(useCase, times(1)).updateBookmark(ayatDomain, true)
        verify(useCase, times(2)).getAllBookmark()
    }
}
