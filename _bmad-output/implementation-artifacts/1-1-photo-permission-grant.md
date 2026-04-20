# Story 1.1: Photo Permission Grant

Status: ready-for-dev

## Story

As a **user**,
I want **to grant photo library access permission with a clear explanation**,
so that **I can allow the app to display and analyze my photos while understanding the privacy protection**.

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

This story sets up Android permissions for photo access. Key requirements:

1. Display clear privacy explanation before requesting permission
2. Request READ_MEDIA_IMAGES (Android 13+) or READ_EXTERNAL_STORAGE (Android 10-12)
3. Handle permission denial gracefully with re-request option
4. Show empty state with "Grant Permission" CTA if permission denied

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Platform | Native Android (Kotlin) |
| Min SDK | Android 10 (API 29) |
| UI | Jetpack Compose + Material 3 |
| Permissions | ActivityResultContracts |
| Architecture | MVVM + StateFlow |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── ui/
│   ├── gallery/
│   │   ├── GalleryScreen.kt          # Main gallery with permission handling
│   │   └── GalleryViewModel.kt
│   └── components/
│       └── PermissionRequest.kt       # Permission explanation component
├── util/
│   └── PermissionUtils.kt            # Permission helper functions
└── RetrivApplication.kt
```

## Tasks / Subtasks

- [ ] Task 1: Create permission request UI component
  - [ ] 1.1: Design privacy explanation UI (single screen, clear messaging)
  - [ ] 1.2: Implement "Grant Permission" button with Material 3 styling
- [ ] Task 2: Implement permission request flow
  - [ ] 2.1: Add ActivityResultContracts for permission handling
  - [ ] 2.2: Request READ_MEDIA_IMAGES (API 33+) / READ_EXTERNAL_STORAGE (API 29-32)
  - [ ] 2.3: Handle permission result (grant/deny)
- [ ] Task 3: Implement permission denied state
  - [ ] 3.1: Show empty gallery state with "Grant Permission" button
  - [ ] 3.2: Open system settings when "Grant Permission" tapped after denial
- [ ] Task 4: Verification
  - [ ] 4.1: Test on Android 10, 12, 13, 14 emulators
  - [ ] 4.2: Verify empty state displays when permission not granted

## Dev Notes

### Permission Flow

```
┌─────────────────────────────────────────┐
│           App Launch                    │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│    Permission Already Granted?          │
└────────┬────────────────────────┬───────┘
         │ Yes                   │ No
         ▼                       ▼
┌─────────────────┐    ┌─────────────────────────┐
│   Show Gallery  │    │ Show Privacy Explanation│
└─────────────────┘    └───────────┬─────────────┘
                                   │
                                   ▼
                        ┌─────────────────────┐
                        │ User Taps "Grant"   │
                        └──────────┬──────────┘
                                   │
                                   ▼
                        ┌─────────────────────┐
                        │ System Permission   │
                        │ Dialog             │
                        └──────────┬──────────┘
                                   │
                    ┌──────────────┴──────────────┐
                    ▼                             ▼
           ┌───────────────┐            ┌───────────────┐
           │   Granted     │            │    Denied    │
           └───────┬───────┘            └───────┬───────┘
                   │                            │
                   ▼                            ▼
           ┌───────────────┐            ┌───────────────────┐
           │  Show Gallery │            │ Show Empty State  │
           │  with Photos  │            │ + Settings Button │
           └───────────────┘            └───────────────────┘
```

### Privacy Message (REQUIRED)

Display this EXACT message:
> "RetrivAI indexes your photos on this device only. Your photos never leave your phone."

### Android Permission Details

**Android 13+ (API 33+):**
```kotlin
val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_IMAGES
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}
```

**Android 10-12 (API 29-32):**
```kotlin
Manifest.permission.READ_EXTERNAL_STORAGE
```

### Material 3 Styling

- Primary button: FilledButton, Primary color (#1A73E8)
- Text style: Body Large (16sp)
- Touch target: minimum 48dp
- Spacing: 16dp horizontal margins

### Project Structure Notes

- Follow Clean Architecture: UI → ViewModel → UseCase → Repository
- Use Hilt for dependency injection
- StateFlow for reactive UI state
- Material 3 color scheme from UX design (primary: #1A73E8)

### References

- Architecture: `architecture.md` - Technical Stack, Project Structure
- UX Design: `ux-design-specification.md` - Permission Request pattern, Empty State
- Android Permissions: https://developer.android.com/training/permissions/requesting

## Acceptance Criteria

**Given** the user opens RetrivAI for the first time
**When** the app requests photo library permission
**Then** a single clear explanation is displayed: "RetrivAI indexes your photos on this device only. Your photos never leave your phone."
**And** a single "Grant Permission" button is shown

**Given** the user has previously denied permission
**When** they open the app again
**Then** a "Grant Permission" button is shown in the empty gallery view
**And** tapping it opens system permission settings

## Testing Checklist

- [ ] Android 10 (API 29) - READ_EXTERNAL_STORAGE
- [ ] Android 12 (API 32) - READ_EXTERNAL_STORAGE
- [ ] Android 13 (API 33) - READ_MEDIA_IMAGES
- [ ] Android 14 (API 34) - READ_MEDIA_IMAGES
- [ ] Verify empty state shows when permission denied
- [ ] Verify "Grant Permission" opens system settings after denial

## Story Dependencies

- No previous stories required (Epic 1, Story 1)
- This story enables all subsequent Epic 1 stories (1.2-1.6)

---

**Generated:** 2026-04-20
**Epic:** 1
**Story:** 1.1
