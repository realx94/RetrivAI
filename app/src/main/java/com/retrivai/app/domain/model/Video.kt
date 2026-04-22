package com.retrivai.app.domain.model

import android.net.Uri

data class Video(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val duration: Long,
    val dateTaken: Long,
    val size: Long
) {
    val formattedDuration: String
        get() {
            val totalSeconds = duration / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "%d:%02d".format(minutes, seconds)
        }
}