package com.retrivai.app.data.preferences

import android.content.Context
import com.retrivai.app.domain.model.GemmaModel
import com.retrivai.app.domain.model.GridDensity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getGridDensity(): GridDensity =
        prefs.getString(KEY_GRID_DENSITY, null)
            ?.let { runCatching { GridDensity.valueOf(it) }.getOrNull() }
            ?: GridDensity.SMALL

    fun setGridDensity(density: GridDensity) {
        prefs.edit().putString(KEY_GRID_DENSITY, density.name).apply()
    }

    fun isAutoAlbumSuggestionsEnabled(): Boolean =
        prefs.getBoolean(KEY_AUTO_ALBUM, true)

    fun setAutoAlbumSuggestionsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_ALBUM, enabled).apply()
    }

    fun isFaceRecognitionEnabled(): Boolean =
        prefs.getBoolean(KEY_FACE_RECOGNITION, true)

    fun setFaceRecognitionEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FACE_RECOGNITION, enabled).apply()
    }

    fun isIndexingCompleteNotificationEnabled(): Boolean =
        prefs.getBoolean(KEY_NOTIF_INDEXING_COMPLETE, true)

    fun setIndexingCompleteNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_INDEXING_COMPLETE, enabled).apply()
    }

    fun isNewMemoriesNotificationEnabled(): Boolean =
        prefs.getBoolean(KEY_NOTIF_NEW_MEMORIES, true)

    fun setNewMemoriesNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_NEW_MEMORIES, enabled).apply()
    }

    fun isWeeklyRecapNotificationEnabled(): Boolean =
        prefs.getBoolean(KEY_NOTIF_WEEKLY_RECAP, false)

    fun setWeeklyRecapNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_WEEKLY_RECAP, enabled).apply()
    }

    companion object {
        private const val PREFS_NAME = "retrivai_app_prefs"
        private const val KEY_GRID_DENSITY = "grid_density"
        private const val KEY_AUTO_ALBUM = "auto_album_suggestions"
        private const val KEY_FACE_RECOGNITION = "face_recognition"
        private const val KEY_NOTIF_INDEXING_COMPLETE = "notif_indexing_complete"
        private const val KEY_NOTIF_NEW_MEMORIES = "notif_new_memories"
        private const val KEY_NOTIF_WEEKLY_RECAP = "notif_weekly_recap"
    }
}
