---
stepsCompleted: [1, 2, 3, 4]
inputDocuments: ["_bmad-output/planning-artifacts/prd.md", "_bmad-output/planning-artifacts/architecture.md", "_bmad-output/planning-artifacts/ux-design-specification.md"]
workflowStatus: "complete"
---

# RetrivAI - Epic Breakdown

## Overview

This document provides the complete epic and story breakdown for RetrivAI, decomposing the requirements from the PRD, UX Design, and Architecture requirements into implementable stories.

## Requirements Inventory

### Functional Requirements

FR1: Users can grant photo library access permission
FR2: Users can view all photos in a grid layout sorted by date
FR3: Users can view individual photo in full screen
FR4: Users can share photos to external apps
FR5: Users can view videos in a grid layout sorted by date
FR6: Users can play videos within the app
FR7: Users can share videos to external apps
FR8: System generates semantic descriptions for videos similar to photos
FR9: Users can enter natural language queries to search photos and videos
FR10: Users can receive results matching semantic meaning of query
FR11: Users can view search results with relevance indication
FR12: System automatically indexes photos and videos using on-device AI
FR13: System generates descriptive tags for each photo
FR14: System generates descriptive tags for each video
FR15: System identifies people, objects, locations, and events in photos and videos
FR16: System identifies faces and recognizes same person across photos
FR17: Users can name identified faces (e.g., "Mom", "Best friend A")
FR18: Users can name identified pets and objects (e.g., "my dog Lucky", "red car")
FR19: System maintains searchable index of all descriptions and named entities
FR20: System detects device RAM capability
FR21: System selects appropriate Gemma model (1B or 4B) based on device
FR22: Users can manually switch between Gemma models
FR23: Users can select which model to download for storage management
FR24: System suggests albums based on photo and video content
FR25: Users can view system-suggested albums
FR26: System automatically groups similar photos and videos together
FR27: Users can browse content by people, places, or events
FR28: Users can search for photos by person's name (e.g., "photos with Mom")
FR29: Users can search for photos by pet's name (e.g., "photos with my dog Lucky")
FR30: All AI processing occurs on-device without network transmission
FR31: System provides visibility into which AI processes are running
FR32: System never transmits user photos or videos to external servers
FR33: Users can view Privacy Dashboard showing data processing status
FR34: System indexes photos and videos in background
FR35: Users can view indexing progress
FR36: System completes initial indexing within 30 minutes for 2000 photos
FR37: Users can configure indexing priority (battery vs speed)
FR38: Users can configure default search model (1B or 4B)
FR39: Users can enable/disable auto-album suggestions
FR40: Users can configure indexing behavior (Wi-Fi only, while charging, etc.)
FR41: Users can customize grid view density (small, medium, large thumbnails)
FR42: Users can configure notification preferences
FR43: Users can clear AI index and re-index from scratch
FR44: Users can manage named people and pets (rename, delete, merge)
FR45: Users can enable/disable face recognition feature

### NonFunctional Requirements

NFR-P1: Semantic search returns results within 3 seconds on mid-range devices
NFR-P2: Initial indexing completes within 30 minutes for 2000 photos
NFR-P3: App cold start time < 2 seconds
NFR-P4: Smooth scrolling (60fps) in photo grid with 10,000+ items
NFR-P5: Background indexing uses < 20% CPU
NFR-S1: Zero network calls for AI processing (verifiable)
NFR-S2: All user data stored locally only
NFR-S3: No analytics or tracking that sends data external
NFR-S4: User photos never leave device for AI analysis
NFR-S5: Privacy Dashboard shows real-time processing status
NFR-A1: Support for system font scaling
NFR-A2: Touch targets minimum 48dp
NFR-A3: High contrast mode support
NFR-A4: Simple, minimal UI suitable for low-tech users
NFR-B1: Background indexing pauses when battery < 20%
NFR-B2: App storage footprint < 100MB (excluding Gemma model)
NFR-B3: Gemma 1B model size ~1.5GB, 4B model ~3-4GB
NFR-F1: Face recognition processes locally on-device
NFR-F2: Face recognition accuracy > 80% for 10+ photos of same person
NFR-F3: Users can name faces and those names are searchable
NFR-F4: Pet/object recognition identifies common pets (dog, cat) and allows naming

### Additional Requirements

- Starter Template: Android Studio Empty Activity project base
- Key Dependencies: Jetpack Compose BOM, Hilt Android, Room (runtime/ktx/compiler), Coil Compose, WorkManager KTX, Google AI Edge Gallery SDK
- Architecture Pattern: Clean Architecture + MVVM with layer separation (UI → Domain → Data)
- AI Runtime: Google AI Edge Gallery for Gemma 1B/4B model inference
- Privacy Enforcement: Network Security Config blocking all non-Google Play traffic
- Background Processing: WorkManager with battery-aware constraints
- Database: Room with auto-migrations for schema evolution
- State Management: ViewModel + StateFlow for reactive UI
- Dependency Injection: Hilt for official Android DI
- Project Structure: 50+ files organized by feature/layer as defined in architecture.md

### UX Design Requirements

UX-DR1: Implement PhotoGrid component with 3/4-column responsive grid, loading skeleton, tap-to-view, long-press selection
UX-DR2: Implement PrivacyBadge component with shield icon, animated 0-cloud-calls counter, Active/Checking/Error states
UX-DR3: Implement FaceCircle component with circular thumbnail, name label, tap-to-filter interaction
UX-DR4: Implement IndexingProgressRing with circular progress, percentage, battery-aware pausing display
UX-DR5: Implement SearchResultCard with photo thumbnail, relevance bar, matched tags display
UX-DR6: Implement AlbumCard with 2x2 cover grid, album title, photo count, people/location/event variants
UX-DR7: Implement VideoThumbnail with video frame, play icon overlay, duration badge
UX-DR8: Implement FullScreenPhotoViewer with pinch-zoom, pan gestures, swipe navigation, back/share/info controls
UX-DR9: Implement bottom tab navigation (Gallery | Search | Albums | Settings) with active indicator pill
UX-DR10: Implement Material 3 SearchBar with rounded corners, "Search by describing..." placeholder, debounced input
UX-DR11: Implement button hierarchy: Primary (FilledButton), Secondary (OutlinedButton), Tertiary (TextButton), IconButton
UX-DR12: Implement feedback patterns: Snackbar for success/error, CircularProgressIndicator for loading, EmptyState component
UX-DR13: Implement form patterns: Search input debouncing (300ms), Permission request dialog
UX-DR14: Implement navigation patterns: Screen stack with Back arrow, BottomSheet for modals, swipe-to-dismiss
UX-DR15: Implement responsive breakpoints: Compact (<600dp, 3-col), Medium (600-840dp, 4-col), Expanded (>840dp, 5-6-col)
UX-DR16: Implement accessibility: 48dp touch targets, contentDescription, TalkBack support, reduced-motion support
UX-DR17: Implement grid density settings: small (3-col), medium (4-col), large (2-col) user preference

### FR Coverage Map

| FR Category | FRs | Coverage |
|-------------|-----|----------|
| Photo Access | FR1-FR4 | 4/4 |
| Video Access | FR5-FR8 | 4/4 |
| AI Semantic Search | FR9-FR12 | 4/4 |
| AI Understanding | FR13-FR19 | 7/7 |
| Adaptive Model | FR20-FR23 | 4/4 |
| Organization | FR24-FR29 | 6/6 |
| Privacy | FR30-FR33 | 4/4 |
| Indexing | FR34-FR37 | 4/4 |
| Settings | FR38-FR45 | 8/8 |

## Epic List

### Epic 1: Photo & Video Library Access
Users can browse and access their entire photo and video library with basic viewing and sharing capabilities.
**FRs covered:** FR1-FR7

### Epic 2: On-Device AI Indexing
User's photo library is automatically analyzed and indexed locally using on-device AI.
**FRs covered:** FR8, FR12-FR16, FR34-FR37

### Epic 3: Natural Language Semantic Search
Users can find photos by describing what they remember in natural language.
**FRs covered:** FR9-FR11, FR28-FR29

### Epic 4: Face & Object Recognition & Naming
Users can name faces, pets, and objects to enable search by name.
**FRs covered:** FR17-FR19, FR44

### Epic 5: Album Organization & Discovery
Users can discover AI-curated albums organized by people, places, and events.
**FRs covered:** FR24-FR27

### Epic 6: Privacy Dashboard & Verification
Users can verify and trust that all AI processing happens locally without any network transmission.
**FRs covered:** FR30-FR33

### Epic 7: Adaptive Model Management
System automatically selects the best Gemma model for user's device, with manual override options.
**FRs covered:** FR20-FR23

### Epic 8: Settings & Customization
Users can configure app behavior, appearance, and indexing preferences.
**FRs covered:** FR38-FR45

### FR Coverage Map

FR1: Epic 1 - Photo library permission grant
FR2: Epic 1 - Photo grid view sorted by date
FR3: Epic 1 - Full screen photo viewing
FR4: Epic 1 - Share photos to external apps
FR5: Epic 1 - Video grid view sorted by date
FR6: Epic 1 - Video playback within app
FR7: Epic 1 - Share videos to external apps
FR8: Epic 2 - Video semantic descriptions (indexed like photos)
FR9: Epic 3 - Natural language search input
FR10: Epic 3 - Semantic matching results
FR11: Epic 3 - Search results with relevance indication
FR12: Epic 2 - Automatic AI indexing of photos/videos
FR13: Epic 2 - Descriptive tags for photos
FR14: Epic 2 - Descriptive tags for videos
FR15: Epic 2 - Identify people, objects, locations, events
FR16: Epic 2 - Face recognition across photos
FR17: Epic 4 - Name identified faces
FR18: Epic 4 - Name identified pets and objects
FR19: Epic 4 - Searchable index of names and entities
FR20: Epic 7 - Detect device RAM capability
FR21: Epic 7 - Auto-select Gemma model based on device
FR22: Epic 7 - Manual model switching
FR23: Epic 7 - Model download selection for storage
FR24: Epic 5 - AI-suggested albums from content
FR25: Epic 5 - View system-suggested albums
FR26: Epic 5 - Auto-group similar photos/videos
FR27: Epic 5 - Browse by people, places, events
FR28: Epic 3 - Search by person's name
FR29: Epic 3 - Search by pet's name
FR30: Epic 6 - On-device AI processing only
FR31: Epic 6 - Visibility into AI processes running
FR32: Epic 6 - Never transmit photos/videos externally
FR33: Epic 6 - Privacy Dashboard with processing status
FR34: Epic 2 - Background indexing
FR35: Epic 2 - View indexing progress
FR36: Epic 2 - Indexing completes within 30 min for 2000 photos
FR37: Epic 2 - Configure indexing priority
FR38: Epic 8 - Configure default search model
FR39: Epic 8 - Enable/disable auto-album suggestions
FR40: Epic 8 - Configure indexing behavior
FR41: Epic 8 - Customize grid view density
FR42: Epic 8 - Configure notification preferences
FR43: Epic 8 - Clear AI index and re-index
FR44: Epic 4 - Manage named people and pets
FR45: Epic 8 - Enable/disable face recognition

## Epic 1: Photo & Video Library Access

Users can browse and access their entire photo and video library with basic viewing and sharing capabilities.

### Story 1.1: Photo Permission Grant

As a **user**,
I want **to grant photo library access permission with a clear explanation**,
So that **I can allow the app to display and analyze my photos while understanding the privacy protection**.

**Acceptance Criteria:**

**Given** the user opens RetrivAI for the first time
**When** the app requests photo library permission
**Then** a single clear explanation is displayed: "RetrivAI indexes your photos on this device only. Your photos never leave your phone."
**And** a single "Grant Permission" button is shown

**Given** the user has previously denied permission
**When** they open the app again
**Then** a "Grant Permission" button is shown in the empty gallery view
**And** tapping it opens system permission settings

### Story 1.2: Photo Grid View

As a **user**,
I want **to view all my photos in a responsive grid sorted by date**,
So that **I can browse my photo library efficiently**.

**Acceptance Criteria:**

**Given** the user has granted photo permission
**When** they open the Gallery tab
**Then** all photos are displayed in a grid sorted by date (newest first)
**And** grid columns adapt to screen width (3 columns <600dp, 4 columns ≥600dp)
**And** photos load progressively as user scrolls

**Given** the user is viewing the photo grid
**When** a new photo is taken and saved
**Then** it appears at the top of the grid

**Given** the user has no photos
**When** they open the Gallery tab
**Then** an empty state is shown with icon and "No photos yet" message

### Story 1.3: Full Screen Photo View

As a **user**,
I want **to view individual photos in full screen with gesture support**,
So that **I can examine photos closely and navigate easily**.

**Acceptance Criteria:**

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

### Story 1.4: Photo Sharing

As a **user**,
I want **to share photos to external apps with one tap**,
So that **I can send photos to friends and other apps easily**.

**Acceptance Criteria:**

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

### Story 1.5: Video Grid View & Playback

As a **user**,
I want **to view and play videos in a grid layout**,
So that **I can browse and watch my videos just like my photos**.

**Acceptance Criteria:**

**Given** the user has granted photo permission
**When** they open the Gallery tab
**Then** videos are displayed alongside photos in the same grid
**And** each video shows a duration badge (e.g., "2:34")
**And** each video shows a play icon overlay

**Given** the user taps a video thumbnail
**When** the video begins playing inline in the grid
**Then** a mute toggle appears
**And** tapping again opens full screen video player

**Given** the user is watching a video inline or full screen
**When** they tap the back button
**Then** video stops and returns to grid view

### Story 1.6: Video Sharing

As a **user**,
I want **to share videos to external apps with one tap**,
So that **I can send videos to friends and other apps easily**.

**Acceptance Criteria:**

**Given** the user is viewing a video in full screen
**When** they tap the share button
**Then** the system share sheet appears
**And** available external apps are listed

**Given** the user has tapped the share button
**When** they select an external app
**Then** the video is sent to that app
**And** a success snackbar appears: "Video shared successfully"

## Epic 2: On-Device AI Indexing

User's photo library is automatically analyzed and indexed locally using on-device AI.

### Story 2.1: Background Photo Indexing

As a **user**,
I want **my photos to be automatically indexed by AI in the background**,
So that **I can search my photos without manually organizing them**.

**Acceptance Criteria:**

**Given** the user has granted photo permission
**When** the app enters the background and device is charging
**Then** WorkManager starts indexing unindexed photos
**And** indexing uses less than 20% CPU (NFR-P5)

**Given** the indexing is in progress
**When** battery drops below 20%
**Then** indexing pauses automatically (NFR-B1)
**And** resumes when battery is above 20% and device is charging

**Given** the initial indexing is in progress for 2000 photos
**When** the user checks progress
**Then** indexing completes within 30 minutes (NFR-P2)

### Story 2.2: Photo Tagging with AI

As a **user**,
I want **my photos to have AI-generated descriptive tags**,
So that **I can find photos by describing what I remember**.

**Acceptance Criteria:**

**Given** a photo is being indexed
**When** Gemma analyzes the photo
**Then** descriptive tags are generated (e.g., "beach", "sunset", "dog", "family")
**And** people, objects, locations, and events are identified (FR15)

**Given** the AI has generated tags for a photo
**When** the user searches for related content
**Then** the photo appears in results based on semantic matching

### Story 2.3: Video Tagging with AI

As a **user**,
I want **my videos to have AI-generated semantic descriptions**,
So that **I can search videos using natural language just like photos**.

**Acceptance Criteria:**

**Given** a video is being indexed
**When** Gemma analyzes the video frames
**Then** descriptive tags are generated similar to photos (FR8, FR14)
**And** key scenes, objects, and events are identified

**Given** the AI has generated tags for a video
**When** the user searches for related content
**Then** the video appears in results based on semantic matching

### Story 2.4: Face Recognition

As a **user**,
I want **the app to recognize faces across my photos**,
So that **I can find photos of specific people**.

**Acceptance Criteria:**

**Given** a photo is being indexed
**When** faces are detected in the photo
**Then** face regions are extracted and stored locally (NFR-F1)
**And** recognized faces matching existing clusters are linked

**Given** there are 10+ photos of the same person
**When** the face recognition runs
**Then** accuracy is greater than 80% for recognizing that person (NFR-F2)

### Story 2.5: Indexing Progress Display

As a **user**,
I want **to see indexing progress clearly**,
So that **I know how much of my library has been analyzed**.

**Acceptance Criteria:**

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

### Story 2.6: Indexing Configuration

As a **user**,
I want **to configure when and how indexing runs**,
So that **I can balance between speed and battery usage**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they navigate to Indexing settings
**Then** options are shown: "Battery + Wi-Fi", "While Charging", "Always"

**Given** the user selects "Battery + Wi-Fi"
**When** indexing runs
**Then** it only runs when battery > 20% and connected to Wi-Fi

**Given** the user selects "While Charging"
**When** indexing runs
**Then** it only runs when device is plugged in

## Epic 3: Natural Language Semantic Search

Users can find photos by describing what they remember in natural language.

### Story 3.1: Natural Language Search Input

As a **user**,
I want **to type natural language queries to search my photos**,
So that **I can find photos by describing what I remember**.

**Acceptance Criteria:**

**Given** the user is on the Search tab
**When** they tap the search bar
**Then** keyboard appears
**And** placeholder shows "Search by describing what you remember..."

**Given** the user types a query
**When** they type characters
**Then** there is a 300ms debounce before processing
**And** input is sent for semantic analysis

### Story 3.2: Semantic Search Results

As a **user**,
I want **to receive search results matching the semantic meaning of my query**,
So that **I find the exact photo I'm looking for**.

**Acceptance Criteria:**

**Given** the user submits a search query
**When** results are displayed
**Then** they appear within 3 seconds on mid-range devices (NFR-P1)
**And** results are ranked by relevance

**Given** the user searches for "my dog at the beach"
**When** results appear
**Then** photos containing dogs at beaches are shown first
**And** semantic understanding works (not just keyword matching)

### Story 3.3: Search by Person or Pet Name

As a **user**,
I want **to search for photos by naming a person or pet**,
So that **I can quickly find photos with specific individuals**.

**Acceptance Criteria:**

**Given** the user has named a face "Mom" (FR17)
**When** they search for "photos with Mom"
**Then** all photos containing that face are returned (FR28)

**Given** the user has named a pet "Lucky" (FR18)
**When** they search for "photos with my dog Lucky"
**Then** all photos containing that dog are returned (FR29)

### Story 3.4: Search Results with Relevance Indicator

As a **user**,
I want **to see relevance indication on search results**,
So that **I can quickly identify the best matches**.

**Acceptance Criteria:**

**Given** search results are displayed
**When** the user views each result
**Then** a green relevance bar shows match confidence (0-100%)
**And** the bar width is proportional to match score

**Given** the user hovers over or selects a result
**When** they view it
**Then** matched tags are highlighted below the photo

## Epic 4: Face & Object Recognition & Naming

Users can name faces, pets, and objects to enable search by name.

### Story 4.1: Name Identified Faces

As a **user**,
I want **to name faces identified in my photos**,
So that **I can search for photos by people's names**.

**Acceptance Criteria:**

**Given** the app has identified a face cluster
**When** the user views the People section in Albums
**Then** the face is shown as "Person 1", "Person 2", etc.

**Given** the user taps an unnamed face
**When** they enter a name like "Mom" or "Best friend A"
**Then** the name is saved and searchable (FR17, NFR-F3)

### Story 4.2: Name Identified Pets and Objects

As a **user**,
I want **to name pets and objects identified in my photos**,
So that **I can search for them by name**.

**Acceptance Criteria:**

**Given** the app has identified a dog in photos
**When** the user taps the identified pet
**Then** they can name it "my dog Lucky" (FR18)

**Given** the user has named an object like "red car"
**When** they search for that name
**Then** all photos containing that object are returned

### Story 4.3: Manage Named People and Pets

As a **user**,
I want **to manage named people and pets (rename, delete, merge)**,
So that **I can keep my library organized**.

**Acceptance Criteria:**

**Given** the user is in Settings > People
**When** they tap a named person
**Then** options appear: Rename, Delete, Merge

**Given** the user selects Merge
**When** they select two people to merge
**Then** the faces are combined into one person
**And** search works with either original name

**Given** the user selects Delete
**When** they confirm deletion
**Then** the name is removed
**And** faces return to unnamed state

## Epic 5: Album Organization & Discovery

Users can discover AI-curated albums organized by people, places, and events.

### Story 5.1: AI-Suggested Albums

As a **user**,
I want **the app to suggest albums based on my photo content**,
So that **I can rediscover memories automatically**.

**Acceptance Criteria:**

**Given** the app has indexed a significant number of photos
**When** the user opens the Albums tab
**Then** AI-curated albums appear based on content (FR24)

**Given** the user has auto-album suggestions enabled
**When** new photos are indexed
**Then** albums automatically update with new content

### Story 5.2: View System Albums

As a **user**,
I want **to view albums organized by people, places, and events**,
So that **I can easily browse my memories**.

**Acceptance Criteria:**

**Given** the user opens the Albums tab
**When** they scroll down
**Then** albums are organized into categories: People, Places, Events

**Given** the user taps a People album
**When** it contains named faces
**Then** face circles with names are shown
**And** tapping a face filters photos by that person

### Story 5.3: Browse by People, Places, Events

As a **user**,
I want **to browse my photos by people, places, or events**,
So that **I can find specific content quickly**.

**Acceptance Criteria:**

**Given** the user is on the Albums tab
**When** they tap a People category
**Then** all recognized faces are shown as face circles

**Given** the user is on the Albums tab
**When** they tap a Places category
**Then** albums are grouped by detected locations

**Given** the user is on the Albums tab
**When** they tap an Events category
**Then** albums are grouped by detected events or dates

## Epic 6: Privacy Dashboard & Verification

Users can verify and trust that all AI processing happens locally without any network transmission.

### Story 6.1: Privacy Dashboard Display

As a **user**,
I want **to view a Privacy Dashboard showing data processing status**,
So that **I can verify my photos are processed locally**.

**Acceptance Criteria:**

**Given** the user opens the Settings tab
**When** they tap Privacy Dashboard
**Then** a screen shows "Privacy Protection Active"
**And** "0 Cloud Calls Blocked" counter is displayed

**Given** the user is on the Privacy Dashboard
**When** they view the status
**Then** a shield icon with green color indicates active protection

### Story 6.2: Real-Time Cloud Call Counter

As a **user**,
I want **to see the cloud call counter increment in real-time**,
So that **I can verify no photos are being sent to servers**.

**Acceptance Criteria:**

**Given** the user is on the Privacy Dashboard
**When** AI processing occurs on-device
**Then** the "Cloud Calls Blocked" counter remains at 0

**Given** the user has Network Security Config blocking non-Google Play traffic
**When** any external network call is attempted
**Then** the call is blocked
**And** the counter increments (visible proof of blocking)

### Story 6.3: On-Device Processing Only

As a **user**,
I want **all AI processing to happen on my device**,
So that **my photos never leave my phone**.

**Acceptance Criteria:**

**Given** the app is analyzing photos with Gemma
**When** the analysis is running
**Then** all processing happens via Google AI Edge Gallery locally
**And** no photos are transmitted to external servers (FR30, FR32)

**Given** the app has Network Security Config configured
**When** the app is installed
**Then** all non-Google Play network traffic is blocked
**And** this is verifiable by the user via Privacy Dashboard

## Epic 7: Adaptive Model Management

System automatically selects the best Gemma model for user's device, with manual override options.

### Story 7.1: Device RAM Detection

As a **system**,
I want **to detect the device's RAM capability**,
So that **I can select the appropriate Gemma model**.

**Acceptance Criteria:**

**Given** the app is starting for the first time
**When** it initializes
**Then** device RAM is detected via ActivityManager.getLargeMemoryClass()

**Given** device has 4GB+ RAM
**When** model selection occurs
**Then** Gemma 1B is selected by default

**Given** device has 8GB+ RAM
**When** model selection occurs
**Then** Gemma 4B is selected by default (better quality)

### Story 7.2: Automatic Model Selection

As a **system**,
I want **to automatically select the Gemma model based on device capability**,
So that **optimal performance is achieved for each device**.

**Acceptance Criteria:**

**Given** the app has detected device RAM
**When** the user opens the app
**Then** the appropriate Gemma model is pre-selected in settings

**Given** the user has a 4GB RAM device
**When** they search
**Then** Gemma 1B runs for fast, efficient inference

**Given** the user has an 8GB+ RAM device
**When** they search
**Then** Gemma 4B runs for higher quality semantic understanding

### Story 7.3: Manual Model Switching

As a **user**,
I want **to manually switch between Gemma models**,
So that **I can choose quality vs. speed based on my preference**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they navigate to AI Model settings
**Then** options show: "Gemma 1B (Faster)" and "Gemma 4B (Higher Quality)"

**Given** the user switches from 1B to 4B
**When** the change is saved
**Then** next search uses Gemma 4B

**Given** the user switches to a model not downloaded
**When** they attempt to save
**Then** a prompt appears offering to download the model

### Story 7.4: Model Download Management

As a **user**,
I want **to select which Gemma model to download for storage management**,
So that **I can manage device storage effectively**.

**Acceptance Criteria:**

**Given** the user is in Settings > AI Model
**When** they view available models
**Then** each model shows size: "Gemma 1B (~1.5GB)" or "Gemma 4B (~3-4GB)"

**Given** the user is on a device with limited storage
**When** they select which model to download
**Then** the selected model is downloaded
**And** other model can be deleted to free space

## Epic 8: Settings & Customization

Users can configure app behavior, appearance, and indexing preferences.

### Story 8.1: Default Search Model Configuration

As a **user**,
I want **to configure the default search model (1B or 4B)**,
So that **I can control AI quality vs. speed**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they navigate to "Default AI Model"
**Then** current selection is indicated
**And** user can tap to change

### Story 8.2: Auto-Album Suggestions Toggle

As a **user**,
I want **to enable or disable auto-album suggestions**,
So that **I can control how actively the app organizes my photos**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they toggle "Auto-Album Suggestions"
**Then** the setting is saved
**And** albums are only shown when feature is enabled (FR39)

### Story 8.3: Grid View Density Customization

As a **user**,
I want **to customize grid view density**,
So that **I can see more or fewer photos per screen**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they navigate to "Grid Density"
**Then** options show: Small (3 columns), Medium (4 columns), Large (2 columns)

**Given** the user selects a density
**When** they return to Gallery
**Then** the grid reflects the new density (FR41)

### Story 8.4: Clear AI Index and Re-index

As a **user**,
I want **to clear the AI index and re-index from scratch**,
So that **I can start fresh if needed**.

**Acceptance Criteria:**

**Given** the user is in Settings > Privacy
**When** they tap "Clear AI Index"
**Then** a confirmation dialog appears

**Given** the user confirms clearing the index
**When** the action completes
**Then** all AI-generated tags and face data are removed
**And** re-indexing begins from the beginning

### Story 8.5: Notification Preferences

As a **user**,
I want **to configure notification preferences**,
So that **I can control what notifications I receive**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they navigate to Notifications
**Then** options show toggles for: "Indexing Complete", "New Memories", "Weekly Recap"

**Given** the user disables all notifications
**When** indexing completes
**Then** no notification is sent

### Story 8.6: Face Recognition Toggle

As a **user**,
I want **to enable or disable face recognition**,
So that **I can opt out of this feature if I prefer**.

**Acceptance Criteria:**

**Given** the user is in Settings
**When** they toggle "Face Recognition"
**Then** the setting is saved

**Given** the user disables face recognition
**When** photos are indexed
**Then** no face detection or clustering occurs
**And** privacy is enhanced (FR45)


