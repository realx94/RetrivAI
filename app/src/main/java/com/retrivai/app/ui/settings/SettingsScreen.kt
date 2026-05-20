package com.retrivai.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.retrivai.app.domain.model.GemmaModel
import com.retrivai.app.domain.model.GridDensity
import com.retrivai.app.domain.model.IndexingMode
import com.retrivai.app.ui.components.IndexingProgressRing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToPrivacy: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Indexing section
            Text(
                text = "AI Indexing",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IndexingProgressRing(
                        progress = uiState.indexingProgress,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (uiState.indexingProgress.state) {
                            is com.retrivai.app.domain.model.IndexingProgressState.Indexing -> "Indexing in progress..."
                            is com.retrivai.app.domain.model.IndexingProgressState.Completed -> "Library indexed"
                            is com.retrivai.app.domain.model.IndexingProgressState.PausedLowBattery -> "Indexing paused"
                            is com.retrivai.app.domain.model.IndexingProgressState.Idle -> "Not started"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IndexingModeSelector(
                        selectedMode = uiState.indexingMode,
                        onModeSelected = { viewModel.onIndexingModeSelected(it) }
                    )
                }
            }

            // AI Model section (Epic 7)
            Text(
                text = "AI Model",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recommended for your device: ${uiState.detectedDefaultModel.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GemmaModel.entries.forEach { model ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onModelSelected(model) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = model == uiState.selectedModel,
                                onClick = { viewModel.onModelSelected(model) }
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = model.displayName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${model.description} · ${model.fileSize}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                    if (!uiState.isModelDownloaded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Model not yet downloaded. It will be fetched on first use.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Display section (Epic 8)
            Text(
                text = "Display",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Grid Density", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    GridDensity.entries.forEach { density ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onGridDensitySelected(density) }
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = density == uiState.gridDensity,
                                onClick = { viewModel.onGridDensitySelected(density) }
                            )
                            Text(
                                text = density.name.lowercase().replaceFirstChar { it.uppercase() } +
                                        " (${density.columns} columns)",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            // Features section (Epic 8)
            Text(
                text = "Features",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SettingsToggleRow(
                        label = "Auto-generate albums",
                        description = "Automatically create albums from AI tags",
                        checked = uiState.autoAlbumEnabled,
                        onCheckedChange = { viewModel.onAutoAlbumToggled(it) }
                    )
                    SettingsToggleRow(
                        label = "Face recognition",
                        description = "Detect and cluster faces in photos",
                        checked = uiState.faceRecognitionEnabled,
                        onCheckedChange = { viewModel.onFaceRecognitionToggled(it) }
                    )
                }
            }

            // Notifications section (Epic 8)
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SettingsToggleRow(
                        label = "Indexing complete",
                        description = "Notify when photo indexing finishes",
                        checked = uiState.notifyIndexingComplete,
                        onCheckedChange = { viewModel.onNotifyIndexingCompleteToggled(it) }
                    )
                    SettingsToggleRow(
                        label = "New faces detected",
                        description = "Notify when new people are detected",
                        checked = uiState.notifyNewFaces,
                        onCheckedChange = { viewModel.onNotifyNewFacesToggled(it) }
                    )
                    SettingsToggleRow(
                        label = "New albums created",
                        description = "Notify when new albums are suggested",
                        checked = uiState.notifyNewAlbums,
                        onCheckedChange = { viewModel.onNotifyNewAlbumsToggled(it) }
                    )
                }
            }

            // Privacy section
            Text(
                text = "Privacy",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToPrivacy() },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Privacy Dashboard", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "View on-device processing statistics",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Text(">", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun IndexingModeSelector(
    selectedMode: IndexingMode,
    onModeSelected: (IndexingMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "When to Index",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        IndexingMode.entries.forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onModeSelected(mode) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = mode == selectedMode,
                    onClick = { onModeSelected(mode) }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = mode.label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = mode.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "AI Indexing",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IndexingProgressRing(
                        progress = uiState.indexingProgress,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (uiState.indexingProgress.state) {
                            is com.retrivai.app.domain.model.IndexingProgressState.Indexing -> "Indexing in progress..."
                            is com.retrivai.app.domain.model.IndexingProgressState.Completed -> "Library indexed"
                            is com.retrivai.app.domain.model.IndexingProgressState.PausedLowBattery -> "Indexing paused"
                            is com.retrivai.app.domain.model.IndexingProgressState.Idle -> "Not started"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IndexingModeSelector(
                        selectedMode = uiState.indexingMode,
                        onModeSelected = { viewModel.onIndexingModeSelected(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun IndexingModeSelector(
    selectedMode: IndexingMode,
    onModeSelected: (IndexingMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "When to Index",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        IndexingMode.entries.forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onModeSelected(mode) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = mode == selectedMode,
                    onClick = { onModeSelected(mode) }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = mode.label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = mode.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}