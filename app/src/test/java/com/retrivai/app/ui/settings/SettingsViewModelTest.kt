package com.retrivai.app.ui.settings

import app.cash.turbine.test
import com.retrivai.app.data.preferences.AppPreferences
import com.retrivai.app.data.preferences.IndexingPreferences
import com.retrivai.app.data.preferences.ModelPreferences
import com.retrivai.app.domain.model.GemmaModel
import com.retrivai.app.domain.model.GridDensity
import com.retrivai.app.domain.model.IndexingMode
import com.retrivai.app.domain.model.IndexingState
import com.retrivai.app.domain.repository.IndexingRepository
import com.retrivai.app.worker.IndexingManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var indexingRepository: IndexingRepository

    @Mock
    private lateinit var indexingPreferences: IndexingPreferences

    @Mock
    private lateinit var indexingManager: IndexingManager

    @Mock
    private lateinit var appPreferences: AppPreferences

    @Mock
    private lateinit var modelPreferences: ModelPreferences

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        `when`(indexingRepository.getIndexingState()).thenReturn(flowOf(null))
        `when`(indexingPreferences.getIndexingMode()).thenReturn(IndexingMode.BATTERY_AND_WIFI)
        `when`(appPreferences.getGridDensity()).thenReturn(GridDensity.MEDIUM)
        `when`(appPreferences.isAutoAlbumEnabled()).thenReturn(true)
        `when`(appPreferences.isFaceRecognitionEnabled()).thenReturn(true)
        `when`(appPreferences.isNotifyIndexingComplete()).thenReturn(true)
        `when`(appPreferences.isNotifyNewFaces()).thenReturn(true)
        `when`(appPreferences.isNotifyNewAlbums()).thenReturn(false)
        `when`(modelPreferences.getSelectedModel()).thenReturn(GemmaModel.GEMMA_1B)
        `when`(modelPreferences.isModelDownloaded(GemmaModel.GEMMA_1B)).thenReturn(false)
        `when`(modelPreferences.detectDefaultModel()).thenReturn(GemmaModel.GEMMA_1B)
        viewModel = SettingsViewModel(
            indexingRepository, indexingPreferences, indexingManager,
            appPreferences, modelPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads persisted indexing mode`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(IndexingMode.BATTERY_AND_WIFI, viewModel.uiState.value.indexingMode)
    }

    @Test
    fun `onIndexingModeSelected persists mode and updates state`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIndexingModeSelected(IndexingMode.WHILE_CHARGING)

        verify(indexingPreferences).setIndexingMode(IndexingMode.WHILE_CHARGING)
        assertEquals(IndexingMode.WHILE_CHARGING, viewModel.uiState.value.indexingMode)
    }

    @Test
    fun `onIndexingModeSelected reschedules indexing with new mode`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIndexingModeSelected(IndexingMode.ALWAYS)

        verify(indexingManager).schedulePeriodicIndexing(IndexingMode.ALWAYS)
    }

    @Test
    fun `onIndexingModeSelected to BATTERY_AND_WIFI persists and reschedules`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIndexingModeSelected(IndexingMode.BATTERY_AND_WIFI)

        verify(indexingPreferences).setIndexingMode(IndexingMode.BATTERY_AND_WIFI)
        verify(indexingManager).schedulePeriodicIndexing(IndexingMode.BATTERY_AND_WIFI)
        assertEquals(IndexingMode.BATTERY_AND_WIFI, viewModel.uiState.value.indexingMode)
    }

    @Test
    fun `initial state has idle indexing progress when no state in db`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.indexingProgress.percentage)
    }
}
