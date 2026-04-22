package com.retrivai.app.ui.gallery.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.model.Video

sealed class MediaItem {
    abstract val id: Long

    data class PhotoItem(val photo: Photo) : MediaItem() {
        override val id: Long get() = photo.id
    }

    data class VideoItem(val video: Video) : MediaItem() {
        override val id: Long get() = video.id
    }
}

@Composable
fun MediaGrid(
    photos: List<Photo>,
    videos: List<Video>,
    onPhotoClick: (Int) -> Unit,
    onPhotoLongPress: (Photo) -> Unit = {},
    onVideoClick: (Int) -> Unit = {},
    isSelectionMode: Boolean = false,
    selectedPhotoIds: Set<Long> = emptySet(),
    playingVideoId: Long? = null,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val columns = when {
        screenWidthDp < 600 -> 3
        screenWidthDp < 840 -> 4
        else -> 5
    }

    val mediaItems = buildList {
        photos.forEach { add(MediaItem.PhotoItem(it)) }
        videos.forEach { add(MediaItem.VideoItem(it)) }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(mediaItems, key = { it.id }) { item ->
            when (item) {
                is MediaItem.PhotoItem -> {
                    PhotoGridItem(
                        photo = item.photo,
                        onClick = {
                            val index = photos.indexOf(item.photo)
                            onPhotoClick(index)
                        },
                        onLongPress = { onPhotoLongPress(item.photo) },
                        isSelectionMode = isSelectionMode,
                        isSelected = selectedPhotoIds.contains(item.photo.id)
                    )
                }
                is MediaItem.VideoItem -> {
                    val index = videos.indexOf(item.video)
                    VideoGridItem(
                        video = item.video,
                        onClick = { onVideoClick(index) },
                        isPlaying = playingVideoId == item.video.id
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotoGridItem(
    photo: Photo,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    isSelectionMode: Boolean,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(2.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = photo.uri,
            contentDescription = photo.displayName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isSelectionMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoGridItem(
    video: Video,
    onClick: () -> Unit,
    isPlaying: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(2.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = video.uri,
            contentDescription = video.displayName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (!isPlaying) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = video.formattedDuration,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}