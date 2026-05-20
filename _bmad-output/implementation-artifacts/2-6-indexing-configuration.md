# Story 2.6: Indexing Configuration

Status: review

## Story

As a **user**,
I want **to configure when and how indexing runs**,
so that **I can balance between speed and battery usage**.

## Context Summary

**Epic:** 2 - On-Device AI Indexing
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM
**Previous Story:** 2.5 - Indexing Progress Display (Status: review)

## Key Constraints

- **Privacy-First:** All AI processing happens on-device (NFR-S1)
- **Battery-Aware:** Background indexing pauses at <20% battery (NFR-B1, NFR-B2)
- **No New Dependencies:** Use WorkManager `Constraints` API (already in deps); use SharedPreferences for preference persistence — NO DataStore needed (not in build.gradle.kts)
- **WorkManager Rescheduling:** When mode changes, cancel and re-enqueue periodic work with `ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE`

## Acceptance Criteria

1. **Given** the user is in Settings  
   **When** they view the Indexing section  
   **Then** three mode options are displayed: "Battery + Wi-Fi", "While Charging", "Always"  
   **And** the currently active mode is visually highlighted/selected

2. **Given** the user selects "Battery + Wi-Fi"  
   **When** indexing runs  
   **Then** it only runs when battery > 20% (`setRequiresBatteryNotLow(true)`) AND connected to Wi-Fi (`setRequiredNetworkType(NetworkType.UNMETERED)`)

3. **Given** the user selects "While Charging"  
   **When** indexing runs  
   **Then** it only runs when the device is plugged in (`setRequiresCharging(true)`)

4. **Given** the user changes the indexing mode  
   **When** the new mode is selected  
   **Then** the preference is persisted and survives app restarts  
   **And** the WorkManager periodic work is rescheduled immediately with new constraints

5. **Given** the user selects "Always"  
   **When** indexing runs  
   **Then** it runs without battery or Wi-Fi constraints (empty `Constraints`)

## Tasks / Subtasks

- [x] Task 1: Create IndexingMode domain model (AC: 1, 2, 3, 5)
  - [x] 1.1: Create `IndexingMode` enum with BATTERY_AND_WIFI, WHILE_CHARGING, ALWAYS values
  - [x] 1.2: Add display label and description properties to each mode

- [x] Task 2: Create IndexingPreferences data layer (AC: 4)
  - [x] 2.1: Create `IndexingPreferences` class using SharedPreferences (in `data/preferences/`)
  - [x] 2.2: Implement `getIndexingMode(): IndexingMode` using stored string key, defaulting to `BATTERY_AND_WIFI`
  - [x] 2.3: Implement `setIndexingMode(mode: IndexingMode)` to persist the selection
  - [x] 2.4: `IndexingPreferences` uses `@Singleton @Inject constructor` — Hilt auto-provides it without explicit module entry

- [x] Task 3: Update IndexingManager to apply mode-based constraints (AC: 2, 3, 4, 5)
  - [x] 3.1: Inject `IndexingPreferences` into `IndexingManager`
  - [x] 3.2: Add `buildConstraints(mode: IndexingMode): Constraints` helper method
  - [x] 3.3: Update `schedulePeriodicIndexing()` to accept `mode: IndexingMode` parameter and apply constraints from helper
  - [x] 3.4: Update `startImmediateIndexing()` to also apply mode-based constraints
  - [x] 3.5: Use `ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE` when rescheduling after mode change

- [x] Task 4: Update SettingsViewModel to manage indexing mode (AC: 1, 4)
  - [x] 4.1: Inject `IndexingPreferences` and `IndexingManager` into `SettingsViewModel`
  - [x] 4.2: Load persisted `IndexingMode` on init and set in `_uiState`
  - [x] 4.3: Add `onIndexingModeSelected(mode: IndexingMode)` function that persists mode and reschedules indexing

- [x] Task 5: Update SettingsUiState (AC: 1)
  - [x] 5.1: Add `indexingMode: IndexingMode = IndexingMode.BATTERY_AND_WIFI` field to `SettingsUiState`

- [x] Task 6: Update SettingsScreen to show mode selector (AC: 1, 2, 3, 5)
  - [x] 6.1: Replace the "More settings coming soon..." placeholder with an `IndexingModeSelector` composable
  - [x] 6.2: Create `IndexingModeSelector` composable showing three `RadioButton` options with labels and descriptions
  - [x] 6.3: Highlight selected mode and wire `onModeSelected` callback to ViewModel
  - [x] 6.4: Wrapped selector in a Card matching the IndexingProgressRing card style

- [x] Task 7: Write unit tests (AC: 1–5)
  - [x] 7.1: Created `src/test/java/com/retrivai/app/` directory structure
  - [x] 7.2: Test `IndexingPreferences`: default is BATTERY_AND_WIFI, get/set round-trip for all modes
  - [x] 7.3: Test `buildConstraints()` in `IndexingManager`: each mode produces correct WorkManager constraints
  - [x] 7.4: Test `SettingsViewModel.onIndexingModeSelected()`: persists mode and triggers rescheduling

## Dev Notes

### IndexingMode Enum

```kotlin
// domain/model/IndexingMode.kt
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
```

### IndexingPreferences (SharedPreferences-based)

```kotlin
// data/preferences/IndexingPreferences.kt
package com.retrivai.app.data.preferences

import android.content.Context
import com.retrivai.app.domain.model.IndexingMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getIndexingMode(): IndexingMode {
        val stored = prefs.getString(KEY_INDEXING_MODE, null)
        return stored?.let { runCatching { IndexingMode.valueOf(it) }.getOrNull() }
            ?: IndexingMode.BATTERY_AND_WIFI
    }

    fun setIndexingMode(mode: IndexingMode) {
        prefs.edit().putString(KEY_INDEXING_MODE, mode.name).apply()
    }

    companion object {
        private const val PREFS_NAME = "retrivai_indexing_prefs"
        private const val KEY_INDEXING_MODE = "indexing_mode"
    }
}
```

> **Note:** `IndexingPreferences` uses `@Singleton` + `@Inject constructor` so Hilt auto-provides it. No explicit `@Provides` needed in modules as long as the class is properly annotated. However, add a `@Provides` in `WorkerModule.kt` if the `@ApplicationContext` injection causes issues (see DI section below).

### IndexingManager Constraints Builder

```kotlin
// Add to IndexingManager:

private fun buildConstraints(mode: IndexingMode): Constraints {
    return when (mode) {
        IndexingMode.BATTERY_AND_WIFI -> Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        IndexingMode.WHILE_CHARGING -> Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        IndexingMode.ALWAYS -> Constraints.Builder().build()
    }
}

fun schedulePeriodicIndexing(mode: IndexingMode = indexingPreferences.getIndexingMode()) {
    val constraints = buildConstraints(mode)
    // ... enqueue with CANCEL_AND_REENQUEUE policy
}
```

**Important WorkManager import needed:**
```kotlin
import androidx.work.NetworkType
import androidx.work.Constraints
```

### Updated SettingsUiState

```kotlin
data class SettingsUiState(
    val indexingProgress: IndexingProgress = IndexingProgress.idle(),
    val indexingMode: IndexingMode = IndexingMode.BATTERY_AND_WIFI,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### SettingsViewModel Changes

```kotlin
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val indexingRepository: IndexingRepository,
    private val indexingPreferences: IndexingPreferences,
    private val indexingManager: IndexingManager
) : ViewModel() {

    init {
        loadIndexingMode()
        observeIndexingState()
    }

    private fun loadIndexingMode() {
        _uiState.update { it.copy(indexingMode = indexingPreferences.getIndexingMode()) }
    }

    fun onIndexingModeSelected(mode: IndexingMode) {
        indexingPreferences.setIndexingMode(mode)
        indexingManager.schedulePeriodicIndexing(mode)
        _uiState.update { it.copy(indexingMode = mode) }
    }
}
```

### IndexingModeSelector Composable (inside SettingsScreen.kt)

```kotlin
@Composable
private fun IndexingModeSelector(
    selectedMode: IndexingMode,
    onModeSelected: (IndexingMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("When to Index", style = MaterialTheme.typography.titleSmall)
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
                    Text(mode.label, style = MaterialTheme.typography.bodyLarge)
                    Text(mode.description, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }
}
```

### DI Wiring

`IndexingPreferences` is `@Singleton` + `@Inject constructor(@ApplicationContext)` — Hilt will auto-inject it. No module entry needed.

`IndexingManager` already exists as `@Singleton @Inject constructor`. Add `IndexingPreferences` as a constructor parameter.

**SettingsViewModel** needs `IndexingPreferences` and `IndexingManager` added to constructor — Hilt handles this automatically via `@HiltViewModel`.

### WorkManager Policy for Rescheduling

When mode changes, use `CANCEL_AND_REENQUEUE` to apply new constraints immediately:
```kotlin
workManager.enqueueUniquePeriodicWork(
    PERIODIC_PHOTO_INDEXING_WORK,
    ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,  // ← changed from KEEP
    periodicPhotoWork
)
```

### Project Structure Notes

New files:
```
app/src/main/java/com/retrivai/app/
├── domain/model/
│   └── IndexingMode.kt                     (NEW)
├── data/preferences/
│   └── IndexingPreferences.kt              (NEW)
└── ui/settings/
    └── SettingsScreen.kt                   (MODIFY - add IndexingModeSelector)
    └── SettingsViewModel.kt                (MODIFY - add mode load/save)
    └── SettingsUiState.kt                  (MODIFY - add indexingMode field)

app/src/main/java/com/retrivai/app/worker/
    └── IndexingManager.kt                  (MODIFY - add buildConstraints(), mode param)

app/src/test/java/com/retrivai/app/
├── data/preferences/
│   └── IndexingPreferencesTest.kt          (NEW)
├── worker/
│   └── IndexingManagerTest.kt              (NEW)
└── ui/settings/
    └── SettingsViewModelTest.kt            (NEW)
```

### Story Dependencies

- **Requires:** Story 2.1 (Background Photo Indexing) — `IndexingManager`, `WorkManager` setup
- **Requires:** Story 2.5 (Indexing Progress Display) — `SettingsScreen`, `SettingsViewModel`, `SettingsUiState` all exist and are in review
- **Note:** The SettingsScreen placeholder "More settings coming soon..." is the exact injection point for this feature

### References

- `[Source: app/src/main/java/com/retrivai/app/worker/IndexingManager.kt]` — existing scheduling logic with hardcoded constraints
- `[Source: app/src/main/java/com/retrivai/app/ui/settings/SettingsScreen.kt#L98]` — "More settings coming soon..." placeholder to replace
- `[Source: app/src/main/java/com/retrivai/app/ui/settings/SettingsViewModel.kt]` — constructor to extend with new dependencies
- `[Source: app/src/main/java/com/retrivai/app/ui/settings/SettingsUiState.kt]` — data class to extend
- `[Source: app/build.gradle.kts]` — no DataStore dependency; use SharedPreferences
- `[Source: _bmad-output/planning-artifacts/epics.md#Story 2.6]` — acceptance criteria source
- `[Source: _bmad-output/planning-artifacts/architecture.md#Background Processing]` — WorkManager patterns

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.6

### Debug Log References

N/A — IDE language server confirmed no errors on all modified files.

### Completion Notes List

- Story 2.6 fully implemented. All 7 tasks and 20 subtasks completed.
- `IndexingMode.kt` enum created in `domain/model/` with label + description per mode.
- `IndexingPreferences.kt` created in `data/preferences/` using SharedPreferences with safe enum parsing.
- `IndexingManager.kt` updated: added `buildConstraints(mode)`, changed KEEP → CANCEL_AND_REENQUEUE, removed hardcoded constraints, added `IndexingPreferences` injection.
- `SettingsUiState.kt` updated: added `indexingMode` field.
- `SettingsViewModel.kt` updated: added `IndexingPreferences` + `IndexingManager` deps, `loadIndexingMode()` in init, `onIndexingModeSelected()`.
- `SettingsScreen.kt` updated: `IndexingModeSelector` composable added with RadioButton list; placeholder removed.
- `app/build.gradle.kts` updated: added `testImplementation` block with JUnit, Mockito, coroutines-test, Turbine, work-testing.
- 3 test files created: `IndexingPreferencesTest.kt`, `IndexingManagerConstraintsTest.kt`, `SettingsViewModelTest.kt`.
- Build validation via `gradle assembleDebug` failed due to pre-existing Gradle 9.x / Kotlin plugin compatibility issue (`org/gradle/api/internal/HasConvention`) — unrelated to this story.
- IDE analysis (get_errors) returned no errors on all 9 files.

### File List

**New Files:**
- `app/src/main/java/com/retrivai/app/domain/model/IndexingMode.kt`
- `app/src/main/java/com/retrivai/app/data/preferences/IndexingPreferences.kt`
- `app/src/test/java/com/retrivai/app/data/preferences/IndexingPreferencesTest.kt`
- `app/src/test/java/com/retrivai/app/worker/IndexingManagerConstraintsTest.kt`
- `app/src/test/java/com/retrivai/app/ui/settings/SettingsViewModelTest.kt`

**Modified Files:**
- `app/src/main/java/com/retrivai/app/worker/IndexingManager.kt`
- `app/src/main/java/com/retrivai/app/ui/settings/SettingsUiState.kt`
- `app/src/main/java/com/retrivai/app/ui/settings/SettingsViewModel.kt`
- `app/src/main/java/com/retrivai/app/ui/settings/SettingsScreen.kt`
- `app/build.gradle.kts`

### Change Log
