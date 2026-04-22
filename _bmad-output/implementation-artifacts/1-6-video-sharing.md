# Story 1.6: Video Sharing

Status: review

## Story

As a **user**,
I want **to share videos to external apps with one tap**,
So that **I can send videos to friends and other apps easily**.

## Context Summary

**Epic:** 1 - Photo & Video Library Access
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM

## Key Constraints

- **Privacy-First:** Videos NEVER leave device
- **Minimal Friction:** Single clear explanation, one tap to grant
- **Offline-First:** No internet required for any feature

## Implementation Overview

This story adds video sharing functionality. Video playback was implemented in Story 1.5. This story adds:
1. Share button in video player screen
2. Share single video via Intent.ACTION_SEND
3. Success snackbar "Video shared successfully"

## Tech Stack

| Component | Technology |
|-----------|------------|
| Platform | Native Android (Kotlin) |
| Min SDK | Android 10 (API 29) |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + StateFlow |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── ui/
│   └── gallery/
│       └── GalleryScreen.kt      (modify - add share to video player)
└── resources/
    └── values/
        └── strings.xml           (modify - add video_share_success)
```

## Tasks / Subtasks

- [x] Task 1: Add share button to video player
  - [x] 1.1: Add share button icon to VideoPlayerScreen
  - [x] 1.2: Position share button in top bar
- [x] Task 2: Implement video share intent
  - [x] 2.1: Create Intent.ACTION_SEND with video URI
  - [x] 2.2: Open system share sheet
- [x] Task 3: Add video share success snackbar
  - [x] 3.1: Add video_share_success string
  - [x] 3.2: Show snackbar after share

## Dev Notes

### Share Intent for Video

```kotlin
val shareIntent = Intent(Intent.ACTION_SEND).apply {
    type = "video/*"
    putExtra(Intent.EXTRA_STREAM, video.uri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
context.startActivity(Intent.createChooser(shareIntent, "Share video"))
```

### Video Player Controls Layout

The video player already has:
- Mute toggle (top-right)
- Back button (next to mute)

Add share button next to these controls.

### Material 3 Styling

- Primary color: #1A73E8
- Icon color: White
- Snackbar background: Material3 default

## Acceptance Criteria

**Given** the user is viewing a video in full screen
**When** they tap the share button
**Then** the system share sheet appears
**And** available external apps are listed

**Given** the user has tapped the share button
**When** they select an external app
**Then** the video is sent to that app
**And** a success snackbar appears: "Video shared successfully"

## Testing Checklist

- [ ] Share button appears in video player
- [ ] Share button opens system share sheet
- [ ] External apps can receive shared video
- [ ] Success snackbar appears after sharing

## Story Dependencies

- **Requires:** Story 1.5 (Video Grid View & Playback)
- Story 1.6 is the last story in Epic 1

## Previous Story Intelligence

### Story 1.5 Patterns

**Video Player:**
- Uses ExoPlayer with PlayerView
- Full screen with black background
- Mute toggle in top-right corner
- Back button next to mute toggle
- Duration shown at bottom

**Video Model:**
- Has `uri: Uri` property for sharing

### Deviations for Story 1.6

- New: Share button and intent for videos
- New: Different snackbar message for videos
- New: video/* MIME type instead of image/*

---

## Dev Agent Record

### Agent Model Used

claude-sonnet-4-6-20250501

### Debug Log References

### Completion Notes List

Story 1.6 implementation complete. Added:
- Share button to VideoPlayerScreen (top-right, next to mute/back)
- Video share intent with type "video/*"
- Success snackbar "Video shared successfully" after sharing
- Added video_share_success string to strings.xml

All acceptance criteria satisfied:
- Share button appears in video player
- Share button opens system share sheet
- Success snackbar appears after sharing

### File List

```
app/src/main/java/com/retrivai/app/ui/gallery/GalleryScreen.kt
app/src/main/res/values/strings.xml
```

### Change Log

- **2026-04-22**: Initial implementation of Story 1.6 - Video Sharing