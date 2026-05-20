package com.retrivai.app.data.preferences

import android.app.ActivityManager
import android.content.Context
import com.retrivai.app.domain.model.GemmaModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSelectedModel(): GemmaModel =
        prefs.getString(KEY_MODEL, null)
            ?.let { runCatching { GemmaModel.valueOf(it) }.getOrNull() }
            ?: detectDefaultModel()

    fun setSelectedModel(model: GemmaModel) {
        prefs.edit().putString(KEY_MODEL, model.name).apply()
    }

    fun isModelDownloaded(model: GemmaModel): Boolean =
        prefs.getBoolean(downloadedKey(model), false)

    fun setModelDownloaded(model: GemmaModel, downloaded: Boolean) {
        prefs.edit().putBoolean(downloadedKey(model), downloaded).apply()
    }

    private fun downloadedKey(model: GemmaModel) = "downloaded_${model.name}"

    fun detectDefaultModel(): GemmaModel {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)
        val totalRamGb = memInfo.totalMem / (1024L * 1024L * 1024L)
        return if (totalRamGb >= 8) GemmaModel.GEMMA_4B else GemmaModel.GEMMA_1B
    }

    companion object {
        private const val PREFS_NAME = "retrivai_model_prefs"
        private const val KEY_MODEL = "selected_model"
    }
}
