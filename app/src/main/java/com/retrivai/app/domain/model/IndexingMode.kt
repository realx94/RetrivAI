package com.retrivai.app.domain.model

enum class IndexingMode(val label: String, val description: String) {
    BATTERY_AND_WIFI(
        label = "Battery + Wi-Fi",
        description = "Only when battery > 20% and on Wi-Fi"
    ),
    WHILE_CHARGING(
        label = "While Charging",
        description = "Only when plugged in"
    ),
    ALWAYS(
        label = "Always",
        description = "Run without restrictions"
    )
}
