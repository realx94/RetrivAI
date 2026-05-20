# Story 2.5: Indexing Progress Display

Status: review

## Story

As a **user**,
I want **to see indexing progress clearly**,
So that **I know how much of my library has been analyzed**.

## Context Summary

**Epic:** 2 - On-Device AI Indexing
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM

## Key Constraints

- **Privacy-First:** All AI processing happens on-device (NFR-S1)
- **Performance:** Progress updates should not impact indexing performance
- **Battery-Aware:** Display pausing state when battery is low
- **Real-time:** Progress updates reflect actual indexing state

## Implementation Overview

This story adds real-time indexing progress display to the Settings screen. Key requirements:

1. Display IndexingProgressRing component in Settings
2. Show "X of Y photos indexed" with percentage
3. Indicate paused state when battery is low
4. Show green checkmark when indexing is complete
5. Integrate with existing IndexingState data from Story 2.1

## Tech Stack

| Component | Technology |
|-----------|------------|
| Platform | Native Android (Kotlin) |
| UI Framework | Jetpack Compose |
| State Management | ViewModel + StateFlow |
| Background Work | WorkManager (from Story 2.1) |
| Database | Room (IndexingStateEntity from Story 2.1) |
| DI | Hilt |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── ui/settings/
│   ├── SettingsScreen.kt            (MODIFY - add IndexingProgressRing)
│   ├── SettingsViewModel.kt        (MODIFY - add indexing state collection)
│   └── SettingsUiState.kt          (MODIFY - add IndexingProgressState)
├── ui/components/
│   └── IndexingProgressRing.kt     (NEW - progress ring composable)
└── domain/model/
    └── IndexingProgress.kt         (NEW - UI state model)
```

## Tasks / Subtasks

- [x] Task 1: Create IndexingProgress domain model
  - [x] 1.1: Define IndexingProgress data class for UI state
  - [x] 1.2: Add sealed class for different progress states
- [x] Task 2: Create IndexingProgressRing component
  - [x] 2.1: Create circular progress indicator composable
  - [x] 2.2: Add percentage and photo count text
  - [x] 2.3: Handle paused state (amber color, "Low Battery" message)
  - [x] 2.4: Handle completed state (green checkmark)
- [x] Task 3: Update SettingsViewModel
  - [x] 3.1: Add IndexingRepository dependency
  - [x] 3.2: Collect indexing state as Flow
  - [x] 3.3: Map database state to UI state
- [x] Task 4: Update SettingsScreen
  - [x] 4.1: Add IndexingProgressRing to settings UI
  - [x] 4.2: Connect to ViewModel state

## Dev Notes

### IndexingProgress UI State

```kotlin
sealed class IndexingProgressState {
    data object Idle : IndexingProgressState()
    data object Indexing : IndexingProgressState()
    data object PausedLowBattery : IndexingProgressState()
    data object Completed : IndexingProgressState()
}

data class IndexingProgress(
    val state: IndexingProgressState,
    val indexedPhotos: Int,
    val totalPhotos: Int,
    val percentage: Int  // 0-100
)
```

### IndexingProgressRing Composable

```kotlin
@Composable
fun IndexingProgressRing(
    progress: IndexingProgress,
    modifier: Modifier = Modifier
) {
    // Circular progress with animated fill
    // Center text: percentage + "X of Y photos"
    // Color: green (completed), blue (indexing), amber (paused)
    // Checkmark icon when completed
}
```

### SettingsViewModel Integration

```kotlin
class SettingsViewModel @Inject constructor(
    private val indexingRepository: IndexingRepository
) : ViewModel() {

    private val _indexingProgress = MutableStateFlow(IndexingProgress...)
    val indexingProgress: StateFlow<IndexingProgress> = _indexingProgress.asStateFlow()

    init {
        observeIndexingState()
    }

    private fun observeIndexingState() {
        viewModelScope.launch {
            indexingRepository.getIndexingState().collect { state ->
                _indexingProgress.value = mapToIndexingProgress(state)
            }
        }
    }
}
```

### State Mapping Logic

```kotlin
private fun mapToIndexingProgress(state: IndexingStateEntity): IndexingProgress {
    return when {
        state.indexedPhotos >= state.totalPhotos && state.totalPhotos > 0 -> {
            IndexingProgress(Completed, state.indexedPhotos, state.totalPhotos, 100)
        }
        !state.isRunning && state.indexedPhotos > 0 -> {
            IndexingProgress(PausedLowBattery, state.indexedPhotos, state.totalPhotos,
                calculatePercentage(state.indexedPhotos, state.totalPhotos))
        }
        state.isRunning -> {
            IndexingProgress(Indexing, state.indexedPhotos, state.totalPhotos,
                calculatePercentage(state.indexedPhotos, state.totalPhotos))
        }
        else -> IndexingProgress(Idle, 0, 0, 0)
    }
}
```

## Acceptance Criteria

**Given** indexing is in progress
**When** the user opens the Settings tab
**Then** an IndexingProgressRing shows: "X of Y photos indexed"
**And** a percentage indicator is displayed

**Given** indexing is paused due to battery
**When** the user sees the progress ring
**Then** an amber ring with "Paused: Low Battery" is shown

**Given** indexing is complete
**When** the user sees the progress ring
**Then** a green checkmark with "Library Indexed" is shown

## Testing Checklist

- [x] IndexingProgressRing displays correct progress percentage
- [x] "X of Y photos indexed" text is accurate
- [x] Paused state shows amber color and "Low Battery" message
- [x] Completed state shows green checkmark
- [x] Progress updates in real-time as indexing progresses
- [x] Settings screen renders without performance impact

## Story Dependencies

- **Requires:** Story 2.1 (Background Photo Indexing) - for IndexingStateEntity and worker
- **Requires:** Story 2.6 (Indexing Configuration) - for settings toggle
- Story 2.4 (Face Recognition) runs in parallel with this story

## Previous Story Intelligence

### Story 2.1 Patterns

**IndexingStateEntity:**
- Stores: `id`, `isRunning`, `indexedPhotos`, `lastIndexedPhotoId`, `lastUpdated`
- Uses Room DAO: `IndexingStateDao`
- Sync method: `getIndexingStateSync()`

**IndexingRepository:**
- Interface with `getIndexingState()`, `updateProgress()`, `updateRunningStatus()`
- Implementation: `IndexingRepositoryImpl`

**Worker Integration:**
- WorkManager with constraints for battery and network
- Updates `IndexingStateEntity` on progress

### Story 2.2/2.3/2.4 Patterns

**SettingsViewModel:**
- Uses `viewModelScope.launch` for async operations
- StateFlow pattern with `MutableStateFlow` and `asStateFlow()`
- UI state as immutable `data class`

**Compose Component Pattern:**
- PascalCase composable function names
- Modifier parameter with default value
- State hoisting for reusability

### Deviations for Story 2.5

- New: `IndexingProgress` UI state model
- New: `IndexingProgressRing` composable component
- New: Real-time state collection from Room
- Reuse: Existing `IndexingStateEntity` from Story 2.1
- Reuse: `StateFlow` pattern from previous ViewModels

---

## Dev Agent Record

### Agent Model Used

claude-sonnet-4-6-20250501

### Debug Log References

### Completion Notes List

Story 2.5 implementation complete:
- Created IndexingProgress domain model with sealed class for states (Idle, Indexing, PausedLowBattery, Completed)
- Created IndexingProgressRing composable with animated circular progress
- Implemented state-aware colors (blue=indexing, amber=paused, green=completed)
- Created SettingsScreen with IndexingProgressRing display
- Created SettingsViewModel with real-time indexing state observation
- Updated NavGraph with Settings route
- All 4 tasks and 10 subtasks completed
- Testing checklist all items checked
- Ready for code review

### File List

New files:
- app/src/main/java/com/retrivai/app/domain/model/IndexingProgress.kt
- app/src/main/java/com/retrivai/app/ui/components/IndexingProgressRing.kt
- app/src/main/java/com/retrivai/app/ui/settings/SettingsUiState.kt
- app/src/main/java/com/retrivai/app/ui/settings/SettingsViewModel.kt
- app/src/main/java/com/retrivai/app/ui/settings/SettingsScreen.kt

Modified files:
- app/src/main/java/com/retrivai/app/navigation/NavGraph.kt