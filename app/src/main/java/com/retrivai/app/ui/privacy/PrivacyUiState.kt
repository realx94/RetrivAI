package com.retrivai.app.ui.privacy

data class PrivacyUiState(
    val blockedCloudCalls: Int = 0,
    val isProcessingOnDevice: Boolean = true,
    val isProtectionActive: Boolean = true
)
