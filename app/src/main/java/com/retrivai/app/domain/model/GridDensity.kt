package com.retrivai.app.domain.model

enum class GridDensity(val label: String, val columns: Int) {
    SMALL("Small (3 columns)", 3),
    MEDIUM("Medium (4 columns)", 4),
    LARGE("Large (2 columns)", 2)
}
