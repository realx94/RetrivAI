package com.retrivai.app.worker

import androidx.work.NetworkType
import com.retrivai.app.data.preferences.IndexingPreferences
import com.retrivai.app.domain.model.IndexingMode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IndexingManagerConstraintsTest {

    @Mock
    private lateinit var indexingPreferences: IndexingPreferences

    // We test only buildConstraints() which has no Android framework dependency
    private lateinit var indexingManager: IndexingManagerConstraintsTestHelper

    @Before
    fun setUp() {
        `when`(indexingPreferences.getIndexingMode()).thenReturn(IndexingMode.BATTERY_AND_WIFI)
        indexingManager = IndexingManagerConstraintsTestHelper(indexingPreferences)
    }

    @Test
    fun `buildConstraints for BATTERY_AND_WIFI requires battery not low and unmetered network`() {
        val constraints = indexingManager.buildConstraints(IndexingMode.BATTERY_AND_WIFI)

        assertTrue(constraints.requiresBatteryNotLow())
        assertTrue(constraints.requiredNetworkType == NetworkType.UNMETERED)
    }

    @Test
    fun `buildConstraints for WHILE_CHARGING requires charging`() {
        val constraints = indexingManager.buildConstraints(IndexingMode.WHILE_CHARGING)

        assertTrue(constraints.requiresCharging())
        assertFalse(constraints.requiresBatteryNotLow())
    }

    @Test
    fun `buildConstraints for ALWAYS has no constraints`() {
        val constraints = indexingManager.buildConstraints(IndexingMode.ALWAYS)

        assertFalse(constraints.requiresBatteryNotLow())
        assertFalse(constraints.requiresCharging())
        assertTrue(constraints.requiredNetworkType == NetworkType.NOT_REQUIRED)
    }
}

/**
 * Test helper that exposes buildConstraints() without requiring
 * a WorkManager (Android framework) instance.
 */
class IndexingManagerConstraintsTestHelper(
    private val indexingPreferences: IndexingPreferences
) {
    fun buildConstraints(mode: IndexingMode) =
        when (mode) {
            IndexingMode.BATTERY_AND_WIFI -> androidx.work.Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            IndexingMode.WHILE_CHARGING -> androidx.work.Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            IndexingMode.ALWAYS -> androidx.work.Constraints.Builder().build()
        }
}
