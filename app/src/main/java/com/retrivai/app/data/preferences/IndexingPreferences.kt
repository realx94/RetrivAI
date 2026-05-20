package com.retrivai.app.data.preferences

import android.content.Context
import com.retrivai.app.domain.model.IndexingMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getIndexingMode(): IndexingMode {
        val stored = prefs.getString(KEY_INDEXING_MODE, null)
        return stored?.let { runCatching { IndexingMode.valueOf(it) }.getOrNull() }
            ?: IndexingMode.BATTERY_AND_WIFI
    }

    fun setIndexingMode(mode: IndexingMode) {
        prefs.edit().putString(KEY_INDEXING_MODE, mode.name).apply()
    }

    companion object {
        private const val PREFS_NAME = "retrivai_indexing_prefs"
        private const val KEY_INDEXING_MODE = "indexing_mode"
    }
}
