package com.retrivai.app.ui.privacy

import androidx.lifecycle.ViewModel
import com.retrivai.app.data.preferences.PrivacyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val privacyPreferences: PrivacyPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrivacyUiState())
    val uiState: StateFlow<PrivacyUiState> = _uiState.asStateFlow()

    init {
        loadPrivacyState()
    }

    private fun loadPrivacyState() {
        _uiState.update {
            it.copy(blockedCloudCalls = privacyPreferences.getBlockedCloudCallCount())
        }
    }

    fun resetCounter() {
        privacyPreferences.resetBlockedCallCount()
        _uiState.update { it.copy(blockedCloudCalls = 0) }
    }
}
