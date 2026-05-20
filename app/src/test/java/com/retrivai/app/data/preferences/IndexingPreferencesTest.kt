package com.retrivai.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.retrivai.app.domain.model.IndexingMode
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IndexingPreferencesTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var indexingPreferences: IndexingPreferences

    @Before
    fun setUp() {
        `when`(context.getSharedPreferences("retrivai_indexing_prefs", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)
        indexingPreferences = IndexingPreferences(context)
    }

    @Test
    fun `getIndexingMode returns BATTERY_AND_WIFI when no preference stored`() {
        `when`(sharedPreferences.getString("indexing_mode", null)).thenReturn(null)

        val result = indexingPreferences.getIndexingMode()

        assertEquals(IndexingMode.BATTERY_AND_WIFI, result)
    }

    @Test
    fun `getIndexingMode returns BATTERY_AND_WIFI when stored`() {
        `when`(sharedPreferences.getString("indexing_mode", null))
            .thenReturn(IndexingMode.BATTERY_AND_WIFI.name)

        val result = indexingPreferences.getIndexingMode()

        assertEquals(IndexingMode.BATTERY_AND_WIFI, result)
    }

    @Test
    fun `getIndexingMode returns WHILE_CHARGING when stored`() {
        `when`(sharedPreferences.getString("indexing_mode", null))
            .thenReturn(IndexingMode.WHILE_CHARGING.name)

        val result = indexingPreferences.getIndexingMode()

        assertEquals(IndexingMode.WHILE_CHARGING, result)
    }

    @Test
    fun `getIndexingMode returns ALWAYS when stored`() {
        `when`(sharedPreferences.getString("indexing_mode", null))
            .thenReturn(IndexingMode.ALWAYS.name)

        val result = indexingPreferences.getIndexingMode()

        assertEquals(IndexingMode.ALWAYS, result)
    }

    @Test
    fun `getIndexingMode returns default when stored value is invalid`() {
        `when`(sharedPreferences.getString("indexing_mode", null))
            .thenReturn("INVALID_VALUE")

        val result = indexingPreferences.getIndexingMode()

        assertEquals(IndexingMode.BATTERY_AND_WIFI, result)
    }

    @Test
    fun `setIndexingMode persists mode name to SharedPreferences`() {
        indexingPreferences.setIndexingMode(IndexingMode.ALWAYS)

        verify(editor).putString("indexing_mode", IndexingMode.ALWAYS.name)
        verify(editor).apply()
    }

    @Test
    fun `setIndexingMode persists WHILE_CHARGING correctly`() {
        indexingPreferences.setIndexingMode(IndexingMode.WHILE_CHARGING)

        verify(editor).putString("indexing_mode", IndexingMode.WHILE_CHARGING.name)
        verify(editor).apply()
    }

    // Helper to match any non-null string argument
    private fun any(): String = org.mockito.ArgumentMatchers.anyString()
}
