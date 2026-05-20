package com.retrivai.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.retrivai.app.domain.model.IndexingProgress
import com.retrivai.app.domain.model.IndexingProgressState

private val ProgressRingSize: Dp = 120.dp
private val ProgressStrokeWidth: Dp = 8.dp

private val ColorIndexing = Color(0xFF1A73E8)
private val ColorPaused = Color(0xFFFF9800)
private val ColorCompleted = Color(0xFF34A853)
private val ColorBackground = Color(0xFFE0E0E0)

@Composable
fun IndexingProgressRing(
    progress: IndexingProgress,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.percentage / 100f,
        animationSpec = tween(durationMillis = 500),
        label = "progress"
    )

    val ringColor = when (progress.state) {
        is IndexingProgressState.Completed -> ColorCompleted
        is IndexingProgressState.PausedLowBattery -> ColorPaused
        is IndexingProgressState.Indexing -> ColorIndexing
        is IndexingProgressState.Idle -> ColorBackground
    }

    val statusText = when (progress.state) {
        is IndexingProgressState.Completed -> "Library Indexed"
        is IndexingProgressState.PausedLowBattery -> "Paused: Low Battery"
        is IndexingProgressState.Indexing -> "${progress.percentage}%"
        is IndexingProgressState.Idle -> "0%"
    }

    val centerContent = @Composable {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (progress.state) {
                is IndexingProgressState.Completed -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = ColorCompleted,
                        modifier = Modifier.size(32.dp)
                    )
                }
                is IndexingProgressState.Indexing,
                is IndexingProgressState.Idle -> {
                    Text(
                        text = "${progress.percentage}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                is IndexingProgressState.PausedLowBattery -> {
                    Text(
                        text = "${progress.percentage}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ColorPaused
                    )
                }
            }

            Text(
                text = "${progress.indexedPhotos} of ${progress.totalPhotos}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            if (progress.state is IndexingProgressState.PausedLowBattery) {
                Text(
                    text = "Low Battery",
                    style = MaterialTheme.typography.labelSmall,
                    color = ColorPaused
                )
            }
        }
    }

    Box(
        modifier = modifier.size(ProgressRingSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(ProgressRingSize)) {
            val sweepAngle = 360 * animatedProgress
            val stroke = Stroke(
                width = ProgressStrokeWidth.toPx(),
                cap = StrokeCap.Round
            )

            drawArc(
                color = ColorBackground,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            if (progress.percentage > 0) {
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = stroke
                )
            }
        }

        centerContent()
    }

    if (progress.state is IndexingProgressState.Completed) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelMedium,
            color = ColorCompleted,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}