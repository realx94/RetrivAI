package com.retrivai.app.ui.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.retrivai.app.domain.model.Album
import com.retrivai.app.domain.model.AlbumType
import com.retrivai.app.ui.components.AlbumCard
import com.retrivai.app.ui.components.FaceCircle
import com.retrivai.app.ui.people.PeopleScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("People", "Places", "Events", "Suggested")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Albums") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScrollableTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> PeopleTab(uiState.peopleAlbums)
                1 -> AlbumGridTab(uiState.placesAlbums, "No places detected yet")
                2 -> AlbumGridTab(uiState.eventsAlbums, "No events detected yet")
                3 -> AlbumGridTab(uiState.aiSuggestedAlbums, "No suggested albums yet.\nIndex your photos to get started.")
            }
        }
    }
}

@Composable
private fun PeopleTab(peopleAlbums: List<Album>) {
    if (peopleAlbums.isEmpty()) {
        EmptyTabContent("No named people yet.\nTap on faces in the People section to name them.")
    } else {
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(peopleAlbums) { album ->
                FaceCircle(
                    name = album.title,
                    samplePhotoId = album.coverPhotoIds.firstOrNull() ?: 0L,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun AlbumGridTab(albums: List<Album>, emptyMessage: String) {
    if (albums.isEmpty()) {
        EmptyTabContent(emptyMessage)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums) { album ->
                AlbumCard(
                    album = album,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun EmptyTabContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}
