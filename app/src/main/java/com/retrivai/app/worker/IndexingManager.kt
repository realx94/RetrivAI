package com.retrivai.app.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.retrivai.app.data.preferences.IndexingPreferences
import com.retrivai.app.domain.model.IndexingMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val indexingPreferences: IndexingPreferences
) {

    private val workManager = WorkManager.getInstance(context)

    fun buildConstraints(mode: IndexingMode): Constraints {
        return when (mode) {
            IndexingMode.BATTERY_AND_WIFI -> Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            IndexingMode.WHILE_CHARGING -> Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            IndexingMode.ALWAYS -> Constraints.Builder().build()
        }
    }

    fun schedulePeriodicIndexing(mode: IndexingMode = indexingPreferences.getIndexingMode()) {
        val constraints = buildConstraints(mode)

        val periodicPhotoWork = PeriodicWorkRequestBuilder<PhotoIndexingWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag(PhotoIndexingWorker.WORK_TAG)
            .build()

        val periodicVideoWork = PeriodicWorkRequestBuilder<VideoIndexingWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag(VideoIndexingWorker.WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_PHOTO_INDEXING_WORK,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            periodicPhotoWork
        )

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_VIDEO_INDEXING_WORK,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            periodicVideoWork
        )
    }

    fun startImmediateIndexing(mode: IndexingMode = indexingPreferences.getIndexingMode()) {
        val constraints = buildConstraints(mode)

        val immediatePhotoWork = OneTimeWorkRequestBuilder<PhotoIndexingWorker>()
            .setConstraints(constraints)
            .addTag(PhotoIndexingWorker.WORK_TAG)
            .build()

        val immediateVideoWork = OneTimeWorkRequestBuilder<VideoIndexingWorker>()
            .setConstraints(constraints)
            .addTag(VideoIndexingWorker.WORK_TAG)
            .build()

        workManager.enqueueUniqueWork(
            IMMEDIATE_PHOTO_INDEXING_WORK,
            ExistingWorkPolicy.REPLACE,
            immediatePhotoWork
        )

        workManager.enqueueUniqueWork(
            IMMEDIATE_VIDEO_INDEXING_WORK,
            ExistingWorkPolicy.REPLACE,
            immediateVideoWork
        )
    }

    fun cancelAllIndexing() {
        workManager.cancelAllWorkByTag(PhotoIndexingWorker.WORK_TAG)
        workManager.cancelAllWorkByTag(VideoIndexingWorker.WORK_TAG)
    }

    companion object {
        const val PERIODIC_PHOTO_INDEXING_WORK = "periodic_photo_indexing"
        const val PERIODIC_VIDEO_INDEXING_WORK = "periodic_video_indexing"
        const val IMMEDIATE_PHOTO_INDEXING_WORK = "immediate_photo_indexing"
        const val IMMEDIATE_VIDEO_INDEXING_WORK = "immediate_video_indexing"
    }
}