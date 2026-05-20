package com.retrivai.app.ui.people

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.retrivai.app.domain.model.FaceCluster
import com.retrivai.app.ui.components.FaceCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("People") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.faceClusters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No faces detected yet.\nIndex your photos to get started.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.faceClusters) { cluster ->
                    FaceClusterItem(
                        cluster = cluster,
                        onRename = { viewModel.startRenaming(cluster.clusterId) },
                        onDelete = { viewModel.deleteFace(cluster.clusterId) },
                        onMerge = { viewModel.startMerging(cluster.clusterId) }
                    )
                }
            }
        }
    }

    // Rename dialog
    uiState.renamingClusterId?.let { clusterId ->
        val cluster = uiState.faceClusters.find { it.clusterId == clusterId }
        RenameDialog(
            currentName = cluster?.name ?: "",
            onConfirm = { name ->
                viewModel.renameFace(clusterId, name)
                viewModel.cancelRenaming()
            },
            onDismiss = { viewModel.cancelRenaming() }
        )
    }

    // Merge dialog
    uiState.mergingClusterId?.let { sourceId ->
        val otherClusters = uiState.faceClusters.filter { it.clusterId != sourceId }
        MergeDialog(
            source = uiState.faceClusters.find { it.clusterId == sourceId },
            targets = otherClusters,
            onConfirm = { targetId, name ->
                viewModel.mergeFaces(targetId, sourceId, name)
                viewModel.cancelMerging()
            },
            onDismiss = { viewModel.cancelMerging() }
        )
    }
}

@Composable
private fun FaceClusterItem(
    cluster: FaceCluster,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onMerge: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        FaceCircle(
            name = cluster.name ?: "Person ${cluster.clusterId}",
            samplePhotoId = cluster.samplePhotoId,
            onClick = { showMenu = true }
        )
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(text = { Text("Rename") }, onClick = { showMenu = false; onRename() })
            DropdownMenuItem(text = { Text("Merge with...") }, onClick = { showMenu = false; onMerge() })
            DropdownMenuItem(text = { Text("Delete") }, onClick = { showMenu = false; onDelete() })
        }
    }
}

@Composable
private fun RenameDialog(
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Name this person") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name (e.g. Mom, Best friend)") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun MergeDialog(
    source: FaceCluster?,
    targets: List<FaceCluster>,
    onConfirm: (Long, String?) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTarget by remember { mutableStateOf<FaceCluster?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Merge with...") },
        text = {
            Column {
                Text("Select a person to merge with:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                targets.forEach { target ->
                    TextButton(
                        onClick = { selectedTarget = target },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            val isSelected = selectedTarget?.clusterId == target.clusterId
                            Text(
                                text = target.name ?: "Person ${target.clusterId}",
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { selectedTarget?.let { onConfirm(it.clusterId, it.name) } },
                enabled = selectedTarget != null
            ) { Text("Merge") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
