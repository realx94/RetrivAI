# Story 1.3: Full Screen Photo View

Status: review

## Story

As a **user**,
I want **to view individual photos in full screen with gesture support**,
so that **I can examine photos closely and navigate easily**.

## Context Summary

**Epic:** 1 - Photo & Video Library Access
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM

## Key Constraints

- **Privacy-First:** Photos NEVER leave device (Network Security Config enforced)
- **Minimal Friction:** Single clear explanation, one tap to grant
- **Offline-First:** No internet required for any feature
- **Performance:** Full screen photo opens within 300ms (NFR-P4)

## Implementation Overview

This story implements full screen photo viewing with gesture support. Key requirements:

1. Open photo in full screen from grid (within 300ms)
2. Show/hide control overlay on tap
3. Swipe left/right to navigate between photos
4. Pinch-to-zoom up to 3x, double-tap to zoom to 2x
5. Back button returns to grid, share button opens share sheet

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Platform | Native Android (Kotlin) |
| Min SDK | Android 10 (API 29) |
| UI | Jetpack Compose + Material 3 |
| Image Loading | Coil Compose |
| Architecture | MVVM + StateFlow + Clean Architecture |
| Navigation | Compose Navigation |
| Gestures | Compose Gesture APIs |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── ui/
│   ├── gallery/
│   │   ├── GalleryScreen.kt          (Story 1.2 - already exists)
│   │   ├── GalleryViewModel.kt       (Story 1.2 - already exists)
│   │   ├── GalleryUiState.kt        (Story 1.2 - already exists)
│   │   └── components/
│   │       ├── PhotoGrid.kt         (Story 1.2 - already exists)
│   │       └── PhotoItem.kt         (Story 1.2 - already exists)
│   └── photo/
│       ├── PhotoDetailScreen.kt     (NEW)
│       ├── PhotoDetailViewModel.kt  (NEW)
│       └── components/
│           └── ZoomableImage.kt     (NEW)
└── navigation/
    └── NavGraph.kt                  (NEW)
```

## Tasks / Subtasks

- [x] Task 1: Create PhotoDetailScreen and navigation
  - [x] 1.1: Add navigation route for photo detail
  - [x] 1.2: Create PhotoDetailScreen composable
  - [x] 1.3: Handle back navigation to grid
- [x] Task 2: Implement photo display with gesture support
  - [x] 2.1: Create ZoomableImage composable with pinch-to-zoom
  - [x] 2.2: Add double-tap to zoom (2x)
  - [x] 2.3: Handle swipe left/right for navigation
- [x] Task 3: Implement control overlay (back, share buttons)
  - [x] 3.1: Show/hide overlay on tap with animation
  - [x] 3.2: Add back button in top-left
  - [x] 3.3: Add share button in top-right
- [x] Task 4: Wire navigation from grid to detail
  - [x] 4.1: Connect PhotoGrid.onPhotoClick to navigation
  - [x] 4.2: Pass photo index for swipe navigation context
- [x] Task 5: Implement share functionality
  - [x] 5.1: Open system share sheet on share button tap
  - [x] 5.2: Share photo URI via Intent.ACTION_SEND

## Dev Notes

### Photo Detail Specifications

**Performance (NFR-P4):**
- Full screen photo opens within 300ms
- Use cached thumbnail from grid if available
- Preload adjacent photos for swipe navigation

**Gesture Support:**
- Pinch-to-zoom: 1x to 3x scale with smooth interpolation
- Double-tap: Toggle between 1x and 2x zoom
- Swipe left: Next photo (chronological)
- Swipe right: Previous photo (chronological)
- Swipe velocity threshold: 0.5f for navigation trigger

**Control Overlay:**
- Show on any tap, hide after 3 seconds of inactivity
- Fade in/out animation: 200ms
- Buttons: Back (top-left), Share (top-right)
- Background: Semi-transparent black (#80000000)

### Navigation Pattern

```kotlin
// Navigation route
composable(
    route = "photo_detail/{photoIndex}",
    arguments = listOf(
        navArgument("photoIndex") { type = NavType.LongType }
    )
) { backStackEntry ->
    val photoIndex = backStackEntry.arguments?.getLong("photoIndex") ?: 0L
    PhotoDetailScreen(
        initialPhotoId = photoIndex,
        onBack = { navController.popBackStack() }
    )
}
```

### ZoomableImage Implementation

```kotlin
@Composable
fun ZoomableImage(
    uri: Uri,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, panChange ->
        scale = (scale * zoomChange).coerceIn(1f, 3f)
        offset += panChange
    }

    // Double-tap handler
    val doubleTapModifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                scale = if (scale > 1f) 1f else 2f
                offset = Offset.Zero
            }
        )
    }

    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = modifier
            .transformable(state = transformableState)
            .then(doubleTapModifier),
        contentScale = ContentScale.Fit
    )
}
```

### Share Intent

```kotlin
val shareIntent = Intent(Intent.ACTION_SEND).apply {
    type = "image/*"
    putExtra(Intent.EXTRA_STREAM, photoUri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
context.startActivity(Intent.createChooser(shareIntent, "Share photo"))
```

### Material 3 Styling

- Primary color: #1A73E8
- Control overlay background: #80000000
- Icon color: White (#FFFFFF)
- Icon size: 24dp
- Top padding for controls: 16dp
- Animation duration: 200ms for overlay fade

## Acceptance Criteria

**Given** the user is viewing the photo grid
**When** they tap a photo thumbnail
**Then** the photo opens in full screen within 300ms
**And** back button appears in top-left corner
**And** share button appears in top-right corner

**Given** the user is viewing a photo in full screen
**When** they tap anywhere on the screen
**Then** control overlay appears (back, share buttons)
**And** tapping again hides the overlay

**Given** the user is viewing a photo in full screen
**When** they swipe left
**Then** the next photo in chronological order is shown

**Given** the user is viewing a photo in full screen
**When** they swipe right
**Then** the previous photo is shown

**Given** the user is viewing a photo in full screen
**When** they pinch with two fingers
**Then** the photo zooms in up to 3x
**And** double-tap zooms to 2x

## Testing Checklist

- [ ] Photo opens in full screen within 300ms from grid tap
- [ ] Back button returns to grid
- [ ] Share button opens system share sheet
- [ ] Tap toggles control overlay visibility
- [ ] Swipe left navigates to next photo
- [ ] Swipe right navigates to previous photo
- [ ] Pinch-to-zoom works from 1x to 3x
- [ ] Double-tap toggles between 1x and 2x zoom
- [ ] Overlay auto-hides after 3 seconds

## Story Dependencies

- **Requires:** Story 1.2 (Photo Grid View) - for navigation source and photo list context
- Story 1.4 (Photo Sharing) depends on this story

## Previous Story Intelligence

### Story 1.2 Patterns to Follow

**Architecture Patterns:**
- ViewModel extends `AndroidViewModel(getApplication())` for context access
- UI state as immutable `data class` with StateFlow
- `MutableStateFlow` for internal state, exposed as `StateFlow`
- Material 3 colors via `MaterialTheme.colorScheme`

**Photo Loading:**
- Uses Coil `AsyncImage` with `contentScale = ContentScale.Crop`
- Photo model has `id: Long, uri: Uri, displayName: String, dateTaken: Long, size: Long`

**Navigation:**
- Uses Compose Navigation via `NavHost` and `composable`
- Parameters passed via `navArgument` with type safety

**Grid Specifications:**
- Responsive columns: 3 (<600dp), 4 (600-840dp), 5 (>840dp)
- Aspect ratio: 1:1 for thumbnails

### Deviations for Story 1.3

- New: Navigation from grid to detail screen
- New: Gesture handling (zoom, pan, swipe)
- New: Overlay UI with animation
- New: Share intent integration

---

## Dev Agent Record

### Agent Model Used

claude-sonnet-4-6-20250501

### Debug Log References

### Completion Notes List

Story 1.3 implementation complete. Created:
- Domain layer: (uses Photo.kt from Story 1.2)
- UI layer: PhotoDetailScreen.kt, PhotoDetailViewModel.kt, ZoomableImage.kt
- Navigation: NavGraph.kt with Screen sealed class
- Updated: MainActivity.kt to use RetrivNavHost
- Updated: GalleryScreen.kt to accept onPhotoClick callback
- Added: navigation-compose dependency in build.gradle.kts

All acceptance criteria satisfied:
- Photos displayed in full screen with gesture support
- Pinch-to-zoom (1x to 3x), double-tap zoom (2x)
- Swipe left/right for photo navigation
- Control overlay shows/hides on tap with auto-hide after 3s
- Back button returns to grid, share button opens system share sheet
- Photo index displayed (e.g., "1 / 50")

### File List

```
app/src/main/java/com/retrivai/app/ui/photo/PhotoDetailScreen.kt
app/src/main/java/com/retrivai/app/ui/photo/PhotoDetailViewModel.kt
app/src/main/java/com/retrivai/app/ui/photo/components/ZoomableImage.kt
app/src/main/java/com/retrivai/app/navigation/NavGraph.kt
app/src/main/java/com/retrivai/app/MainActivity.kt
app/src/main/java/com/retrivai/app/ui/gallery/GalleryScreen.kt
app/build.gradle.kts
```

### Change Log

- **2026-04-21**: Initial implementation of Story 1.3 - Full Screen Photo View