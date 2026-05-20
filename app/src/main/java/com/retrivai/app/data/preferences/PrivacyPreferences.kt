package com.retrivai.app.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivacyPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getBlockedCloudCallCount(): Int = prefs.getInt(KEY_BLOCKED_CALLS, 0)

    fun incrementBlockedCallCount() {
        val current = getBlockedCloudCallCount()
        prefs.edit().putInt(KEY_BLOCKED_CALLS, current + 1).apply()
    }

    fun resetBlockedCallCount() {
        prefs.edit().putInt(KEY_BLOCKED_CALLS, 0).apply()
    }

    companion object {
        private const val PREFS_NAME = "retrivai_privacy_prefs"
        private const val KEY_BLOCKED_CALLS = "blocked_cloud_calls"
    }
}
