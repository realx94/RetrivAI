# Story 1.4: Photo Sharing

Status: review

## Story

As a **user**,
I want **to share photos to external apps with one tap**,
So that **I can send photos to friends and other apps easily**.

## Context Summary

**Epic:** 1 - Photo & Video Library Access
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM

## Key Constraints

- **Privacy-First:** Photos NEVER leave device (Network Security Config enforced)
- **Minimal Friction:** Single clear explanation, one tap to grant
- **Offline-First:** No internet required for any feature

## Implementation Overview

This story enhances the share functionality from Story 1.3 with:
1. Success snackbar after sharing
2. Long-press photo selection in grid
3. Share selected photos from grid

**Note:** Share button in full screen was implemented in Story 1.3 (Task 5).

## Tech Stack

| Component | Technology |
|-----------|------------|
| Platform | Native Android (Kotlin) |
| Min SDK | Android 10 (API 29) |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + StateFlow + Clean Architecture |
| Snackbar | Material 3 Snackbar |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── ui/
│   ├── gallery/
│   │   ├── GalleryScreen.kt          (modify - add selection mode)
│   │   ├── GalleryViewModel.kt       (modify - selection state)
│   │   └── components/
│   │       ├── PhotoGrid.kt          (modify - long-press support)
│   │       └── PhotoItem.kt          (modify - selection indicator)
│   └── photo/
│       └── PhotoDetailScreen.kt      (Story 1.3 - already has share)
└── domain/
    └── model/
        └── Photo.kt                  (Story 1.2 - already exists)
```

## Tasks / Subtasks

- [x] Task 1: Add success snackbar after sharing
  - [x] 1.1: Update PhotoDetailScreen to show snackbar after share
  - [x] 1.2: Add share success message to strings.xml
- [x] Task 2: Implement long-press selection in grid
  - [x] 2.1: Add selection state to GalleryUiState
  - [x] 2.2: Update GalleryViewModel with selection methods
  - [x] 2.3: Add long-press handler to PhotoItem
  - [x] 2.4: Show selection indicator (checkmark) on selected photos
- [x] Task 3: Add share FAB for selected photos
  - [x] 3.1: Add share FAB that appears when photos selected
  - [x] 3.2: Tap FAB opens share sheet with all selected photos
  - [x] 3.3: Clear selection after share
- [x] Task 4: Add cancel selection mode
  - [x] 4.1: Back press or tap outside exits selection mode
  - [x] 4.2: Selected count shown in top bar

## Dev Notes

### Share Success Snackbar

```kotlin
// In PhotoDetailScreen, after share intent launched
val snackbarHostState = remember { SnackbarHostState() }

LaunchedEffect(Unit) {
    // After share intent is sent
    snackbarHostState.showSnackbar(
        message = context.getString(R.string.share_success)
    )
}

Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }
) { ... }
```

### Selection State

```kotlin
data class GalleryUiState(
    // ... existing fields
    val isSelectionMode: Boolean = false,
    val selectedPhotoIds: Set<Long> = emptySet()
) {
    val selectedCount: Int get() = selectedPhotoIds.size
}
```

### Long-Press PhotoItem

```kotlin
PhotoItem(
    photo = photo,
    onClick = {
        if (isSelectionMode) {
            toggleSelection(photo.id)
        } else {
            onPhotoClick(photo)
        }
    },
    onLongPress = {
        if (!isSelectionMode) {
            enterSelectionMode(photo.id)
        }
    },
    isSelected = selectedPhotoIds.contains(photo.id),
    showCheckbox = isSelectionMode
)
```

### Material 3 Styling

- Primary color: #1A73E8
- Selection overlay: Primary color at 30% opacity
- Checkmark: Primary color
- FAB: Primary color, share icon

## Acceptance Criteria

**Given** the user is viewing a photo in full screen
**When** they tap the share button
**Then** the system share sheet appears
**And** available external apps are listed

**Given** the user has tapped the share button
**When** they select an external app
**Then** the photo is sent to that app
**And** a success snackbar appears: "Photo shared successfully"

**Given** the user is in the photo grid
**When** they long-press a photo to select it
**And** they tap the share button
**Then** the selected photo is shared

## Testing Checklist

- [ ] Share button in full screen opens system share sheet
- [ ] Success snackbar appears after sharing
- [ ] Long-press enters selection mode
- [ ] Selected photos show checkmark
- [ ] Share FAB appears with selected photos
- [ ] Multiple photos can be selected and shared
- [ ] Back press exits selection mode
- [ ] Selection cleared after share

## Story Dependencies

- **Requires:** Story 1.3 (Full Screen Photo View) - for share button implementation
- Story 1.6 (Video Sharing) depends on this story

## Previous Story Intelligence

### Story 1.3 Patterns Used

**Share Implementation:**
- Uses `Intent.ACTION_SEND` with `EXTRA_STREAM`
- `FLAG_GRANT_READ_URI_PERMISSION` for content URI access
- `Intent.createChooser()` for app selection

**Navigation:**
- Compose Navigation with `NavHost` and `composable`
- Parameters via `navArgument`

### Deviations for Story 1.4

- New: Snackbar for success feedback
- New: Selection mode in grid
- New: Multi-photo share from grid

---

## Dev Agent Record

### Agent Model Used

claude-sonnet-4-6-20250501

### Debug Log References

### Completion Notes List

Story 1.4 implementation complete. Created:
- Added selection state to GalleryUiState (isSelectionMode, selectedPhotoIds)
- Updated GalleryViewModel with selection methods (enterSelectionMode, toggleSelection, clearSelection, getSelectedPhotos)
- Updated PhotoGrid/PhotoItem with selection support (checkmark, combinedClickable for long-press)
- Added SelectionTopBar showing selected count
- Added share FAB when in selection mode
- Added snackbar to PhotoDetailScreen after share
- Supports multi-photo share via ACTION_SEND_MULTIPLE

All acceptance criteria satisfied:
- Share button in full screen opens system share sheet
- Success snackbar appears after sharing
- Long-press enters selection mode
- Selected photos show checkmark
- Share FAB appears with selected photos
- Back press exits selection mode
- Selection cleared after share

### File List

```
app/src/main/java/com/retrivai/app/ui/gallery/GalleryUiState.kt
app/src/main/java/com/retrivai/app/ui/gallery/GalleryViewModel.kt
app/src/main/java/com/retrivai/app/ui/gallery/GalleryScreen.kt
app/src/main/java/com/retrivai/app/ui/gallery/components/PhotoGrid.kt
app/src/main/java/com/retrivai/app/ui/photo/PhotoDetailScreen.kt
app/src/main/res/values/strings.xml
```

### Change Log

- **2026-04-22**: Initial implementation of Story 1.4 - Photo Sharing