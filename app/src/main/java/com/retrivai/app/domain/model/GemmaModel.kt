package com.retrivai.app.domain.model

enum class GemmaModel(val label: String, val description: String, val sizeLabel: String) {
    GEMMA_1B("Gemma 1B", "Faster, lower memory usage", "~1.5 GB"),
    GEMMA_4B("Gemma 4B", "Higher quality, more accurate", "~3-4 GB")
}
