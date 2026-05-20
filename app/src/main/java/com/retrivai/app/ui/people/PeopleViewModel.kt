package com.retrivai.app.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.usecase.face.DeleteFaceUseCase
import com.retrivai.app.domain.usecase.face.GetFaceClustersUseCase
import com.retrivai.app.domain.usecase.face.MergeFacesUseCase
import com.retrivai.app.domain.usecase.face.RenameFaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getFaceClustersUseCase: GetFaceClustersUseCase,
    private val renameFaceUseCase: RenameFaceUseCase,
    private val deleteFaceUseCase: DeleteFaceUseCase,
    private val mergeFacesUseCase: MergeFacesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PeopleUiState())
    val uiState: StateFlow<PeopleUiState> = _uiState.asStateFlow()

    init {
        observeFaceClusters()
    }

    private fun observeFaceClusters() {
        getFaceClustersUseCase()
            .onEach { clusters ->
                _uiState.update { it.copy(faceClusters = clusters, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun renameFace(clusterId: Long, name: String?) {
        viewModelScope.launch {
            renameFaceUseCase(clusterId, name?.takeIf { it.isNotBlank() })
        }
    }

    fun deleteFace(clusterId: Long) {
        viewModelScope.launch {
            deleteFaceUseCase(clusterId)
        }
    }

    fun mergeFaces(targetClusterId: Long, sourceClusterId: Long, targetName: String?) {
        viewModelScope.launch {
            mergeFacesUseCase(targetClusterId, sourceClusterId, targetName)
        }
    }

    fun startRenaming(clusterId: Long) {
        _uiState.update { it.copy(renamingClusterId = clusterId) }
    }

    fun cancelRenaming() {
        _uiState.update { it.copy(renamingClusterId = null) }
    }

    fun startMerging(clusterId: Long) {
        _uiState.update { it.copy(mergingClusterId = clusterId) }
    }

    fun cancelMerging() {
        _uiState.update { it.copy(mergingClusterId = null) }
    }
}
