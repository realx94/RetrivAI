package com.retrivai.app.ui.people

import com.retrivai.app.domain.model.FaceCluster

data class PeopleUiState(
    val faceClusters: List<FaceCluster> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val renamingClusterId: Long? = null,
    val mergingClusterId: Long? = null
)
