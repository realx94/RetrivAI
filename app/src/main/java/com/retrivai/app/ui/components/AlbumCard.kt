package com.retrivai.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.retrivai.app.domain.model.Album

@Composable
fun AlbumCard(
    album: Album,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 2x2 cover grid
            val coverIds = album.coverPhotoIds.take(4)
            if (coverIds.size >= 4) {
                Column {
                    Row {
                        AsyncImage(
                            model = "content://media/external/images/media/${coverIds[0]}",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(1f).clip(MaterialTheme.shapes.extraSmall),
                            contentScale = ContentScale.Crop
                        )
                        AsyncImage(
                            model = "content://media/external/images/media/${coverIds[1]}",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(1f).clip(MaterialTheme.shapes.extraSmall),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row {
                        AsyncImage(
                            model = "content://media/external/images/media/${coverIds[2]}",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(1f).clip(MaterialTheme.shapes.extraSmall),
                            contentScale = ContentScale.Crop
                        )
                        AsyncImage(
                            model = "content://media/external/images/media/${coverIds[3]}",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(1f).clip(MaterialTheme.shapes.extraSmall),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else if (coverIds.isNotEmpty()) {
                AsyncImage(
                    model = "content://media/external/images/media/${coverIds[0]}",
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f))
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(
                    text = "${album.itemCount} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
