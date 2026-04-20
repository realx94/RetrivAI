---
stepsCompleted: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
inputDocuments: ["_bmad-output/planning-artifacts/prd.md", "_bmad-output/planning-artifacts/architecture.md"]
lastStep: 14
---

# UX Design Specification RetrivAI

**Author:** RxHunter
**Date:** 2026-04-20

---

## Executive Summary

### Project Vision

RetrivAI is a privacy-first photo management app that uses 100% on-device AI (Gemma) to enable natural language semantic search. Users describe what they're looking for in plain language, and the AI understands meaning - not just metadata. Photos never leave the device.

### Target Users

1. **Minh** - Low-tech office worker with 2000+ photos, frustrated scrolling through library
2. **Linh** - Privacy-first developer who refuses cloud-based photo apps
3. **Ba** - Businessman with 10,000+ photos, rarely revisits memories

### Key Design Challenges

- **Simplicity** - Simple, minimal UI for low-tech users (NFR A4)
- **Trust** - Privacy Dashboard must prove data never leaves device
- **Patience** - Long indexing runs in background; show meaningful progress

### Design Opportunities

- **Fast "aha moment"** - First successful search creates immediate value perception
- **Privacy as feature** - Network block verification as competitive advantage
- **Conversational search** - Natural language queries feel intuitive, not technical

---

## Core User Experience

### Defining Experience

**Core Action:** Semantic Natural Language Search
- Users type queries like "photos with my dog at the beach"
- AI understands meaning, context, and relationships
- Results ranked by relevance with clear indication

**Primary User Flow:**
1. Open app → See photo grid
2. Tap search → Type natural query
3. View results → Find exact photo
4. Tap to view full screen → Share if needed

### Platform Strategy

- **Primary:** Native Android app (Kotlin + Jetpack Compose)
- **Touch-based** - Grid interaction, tap to view, swipe to browse
- **Offline-first** - No internet required for any AI feature
- **Background indexing** - Works while device charges

### Effortless Interactions

- **Search:** Type naturally → Get meaningful results in <3s
- **Permission:** Single clear explanation → One tap to grant
- **Viewing:** Tap thumbnail → Instant full screen
- **Sharing:** One tap → Choose external app

### Critical Success Moments

1. **First successful search** - "dog at beach" returns actual beach dog photos
2. **Privacy verification** - "0 cloud calls" visible on Privacy Dashboard
3. **Album discovery** - AI-curated album matches user's life events
4. **Indexing completion** - User realizes entire library is searchable

### Experience Principles

1. **Instant Gratification** - Results within 3 seconds (NFR P1)
2. **Zero Learning Curve** - No instructions needed
3. **Radical Privacy** - Every screen reinforces local-only processing
4. **Calm Technology** - Background processing, minimal notifications

---

## Desired Emotional Response

### Primary Emotional Goals

**Core Emotion:** "Empowered by AI that respects my privacy"
- Users feel in control of their photo library
- AI works FOR them, not requiring them to organize anything
- Privacy is tangible, not just promised

**Supporting Emotions:**
- **Accomplished** - Found exactly the photo they wanted
- **Delighted** - Surprised search actually understood them
- **Confident** - Trust built through verifiable privacy proof

### Emotional Journey Mapping

| Stage | Emotional Goal | Opposite to Avoid |
|-------|---------------|-------------------|
| First Open | Curious optimism | Overwhelm |
| Permission Request | Trust if clear explanation | Privacy anxiety |
| First Search | Excited "aha!" | Confusion |
| Search Results | Accomplished delight | Frustration |
| Privacy Dashboard | Proud confidence | Skepticism |
| Return Use | Calm efficiency | Anxiety |

### Micro-Emotions

- **Confidence vs. Confusion** - Search UI must be immediately clear
- **Trust vs. Skepticism** - Privacy Dashboard must feel verifiable
- **Excitement vs. Anxiety** - Indexing progress feels exciting, not worrying
- **Accomplishment vs. Frustration** - Finding photo = win moment

### Design Implications

- **Trust** → Privacy Dashboard with real-time "0 cloud calls" counter
- **Accomplishment** → Subtle celebration when search returns perfect results
- **Calm** → Quiet background processing, minimal notification interrupts
- **Confidence** → Clear progress indicators during long indexing

### Emotional Design Principles

1. **Verify Privacy Publicly** - Show the network block counter prominently
2. **Celebrate Small Wins** - Acknowledge when user finds photo quickly
3. **Never Interrupt Calm** - Indexing works silently unless user asks
4. **Make Trust Tangible** - Every search proves AI runs locally

---

## UX Pattern Analysis & Inspiration

### Inspiring Products Analysis

**Google Photos:**
- Core: Search at top, instant results
- Good: Rediscoveries in "Assistant"
- Good: Face naming works well
- Issue: Cloud dependency breaks trust

**Apple Photos:**
- Core: Minimal, clean interface
- Good: "Memories" feature
- Good: Seamless face clustering
- Issue: iOS-only

**Android Gallery:**
- Core: Fast, no dependencies
- Good: Simple grid
- Issue: No search capability

### Transferable UX Patterns

- **Persistent search bar** at top - Google Photos
- **Bottom tab navigation** - Gallery | Search | Albums | Settings
- **Tap to full screen** - instant photo viewing
- **Face thumbnails in circles** - intuitive face UI
- **Progress ring** - calm indexing progress

### Anti-Patterns to Avoid

- Complex folder hierarchies
- Multiple permission prompts
- Aggressive indexing UI
- Cloud upload any prompts

### Design Inspiration Strategy

- Search UX from Google Photos (adapted for natural language)
- Grid simplicity from Apple Photos
- Privacy prominence as differentiator
- Face-based albums as unique value

---

## Design System Foundation

### Design System Choice

**Material Design 3** with Jetpack Compose

### Rationale for Selection

- Built into Jetpack Compose - faster MVP development
- Excellent accessibility defaults (meets NFR A1-A4)
- Easy theming for privacy-first brand (calm colors)
- Google's recommended system for Android AI apps
- Proven components reduce implementation risk

### Implementation Approach

1. Start with Material 3 Compose components
2. Apply custom color scheme via Compose theming
3. Use Material 3 typography scale
4. Extend with custom components where needed

### Customization Strategy

- **Colors:** Calm blue/green palette (trust, privacy)
- **Typography:** System fonts with Material 3 scale
- **Components:** Use Material 3 defaults, minimal custom
- **Spacing:** 8dp grid system
- **Icons:** Material Icons filled style

---

## 2. Core User Experience

### 2.1 Defining Experience

**"Search by describing what you remember"**

The core interaction: User types "my dog at the beach" → AI understands meaning → exact photos appear instantly.

This ONE interaction makes RetrivAI magical. Everything else (albums, faces, privacy) supports this core value.

### 2.2 User Mental Model

- User thinks: "I remember this moment but can't find it"
- User thinks: "I wish I could ask my phone to find it"
- User thinks: "This should be simple"
- System delivers: Semantic search that understands meaning

### 2.3 Success Criteria

1. User finds photo within 10 seconds of typing
2. First result matches intended memory
3. User feels "the AI understood me"
4. Results appear within 3 seconds

### 2.4 Novel UX Patterns

- **Established:** Search bar (Google Photos style)
- **Novel:** Natural language + privacy verification
- No new interaction patterns needed

### 2.5 Experience Mechanics

1. **Initiation:** Tap search bar → keyboard appears
2. **Interaction:** Type natural query → submit
3. **Feedback:** Loading → results within 3s
4. **Completion:** Tap to view full screen → share

---

## Visual Design Foundation

### Color System

**Theme:** Privacy-First (Calm, Trust)

| Token | Hex | Usage |
|-------|-----|-------|
| Primary | #1A73E8 | Trust, security, CTAs |
| Secondary | #34A853 | Calm, growth, success |
| Background | #FFFFFF | Clean, simple |
| Surface | #F8F9FA | Card backgrounds, subtle depth |
| Text Primary | #202124 | Headlines, body text |
| Text Secondary | #5F6368 | Captions, metadata |
| Error | #EA4335 | Error states |
| Divider | #E8EAED | Separators |

**Accessibility:** All color pairs meet WCAG 2.1 AA contrast ratios (4.5:1 for text)

### Typography System

**Font Stack:** System default (Roboto on Android)

**Type Scale (Material 3):**
- Display: 57sp / 64sp line height
- Headline: 32sp / 40sp line height
- Title: 22sp / 28sp line height
- Body: 16sp / 24sp line height
- Label: 14sp / 20sp line height

**Weight Usage:**
- Headlines: 400 (Regular)
- Body: 400 (Regular)
- Buttons: 500 (Medium)

### Spacing & Layout Foundation

**Grid System:** 8dp base unit
- xs: 4dp
- sm: 8dp
- md: 16dp
- lg: 24dp
- xl: 32dp

**Layout Principles:**
- Bottom tab navigation (Gallery | Search | Albums | Settings)
- Card-based content containers
- Consistent 16dp horizontal margins
- Grid: 3-4 columns for photo grid

### Accessibility Considerations

- Minimum touch target: 48dp (NFR A2)
- Support system font scaling (NFR A1)
- High contrast mode support (NFR A3)
- Content descriptions for all interactive elements

---

## Design Direction Decision

### Design Directions Explored

| # | Direction | Key Feature |
|---|----------|-------------|
| 1 | Classic Grid | Google Photos-style 3-column grid |
| 2 | Focus Search | Hero search bar, minimal design |
| 3 | Card-Based | AI album cards, discovery-first |
| 4 | Minimal Dark | Dark mode, maximum content |
| 5 | Privacy Focus | Green/calm, Privacy Dashboard prominent |
| 6 | Face-Centric | People-first navigation, face circles |
| 7 | Album Focus | Albums grid as main entry point |

### Chosen Direction

**Direction 5: Privacy Focus** (recommended base)

Combined with elements from:
- Privacy badge prominent (Direction 5)
- Search bar placement (Direction 1)
- 3-column photo grid (Direction 1)
- Face circles for people (Direction 6)

### Design Rationale

- Privacy-first aesthetic reinforces core brand value
- "0 Cloud Calls" badge creates immediate trust
- Google Photos-style grid is familiar for users
- Face navigation supports "search by person name" (FR28)
- Calm green palette aligns with emotional goal "Radical Privacy"

### Implementation Approach

1. Use Direction 5 green/calm color palette
2. Persistent search bar at top (Direction 1 style)
3. 3-column photo grid below
4. Privacy badge visible on Privacy Dashboard tab
5. Face circles on Albums tab for quick person browsing

---

## Component Strategy

### Design System Components

**Material 3 cung cấp sẵn:**

| Component | RetrivAI Usage |
|-----------|----------------|
| **BottomNavigationBar** | Tab navigation (Gallery, Search, Albums, Settings) |
| **SearchBar** | Semantic search input |
| **Card** | Album cards, search result cards |
| **Button** | CTAs, action buttons |
| **IconButton** | Toolbar actions |
| **TopAppBar** | Screen headers |
| **FloatingActionButton** | Quick actions (if needed) |
| **ListItem** | Settings items, people list |
| **Dialog** | Confirmation dialogs |
| **Snackbar** | Transient feedback |
| **CircularProgressIndicator** | Indexing progress |
| **LinearProgressIndicator** | Download progress |
| **Chip** | Tags, filters |
| **BottomSheet** | Share sheet, detail panels |
| **Divider** | Section separators |

### Custom Components Needed

**1. PhotoGrid Component**
- **Purpose:** Display photos in scrollable 3-column grid
- **Anatomy:** LazyVerticalGrid với PhotoThumbnail items
- **States:** Loading (skeleton), Loaded, Empty ("No photos yet"), Error
- **Variants:** Small (3 columns), Medium (4 columns - default), Large (2 columns)
- **Interaction:** Tap → full screen view, Long press → selection mode
- **Accessibility:** contentDescription for each thumbnail

**2. PrivacyBadge Component**
- **Purpose:** Show real-time "0 Cloud Calls" counter
- **Anatomy:** Shield icon + "0" counter + "Cloud Calls Blocked"
- **States:** Active (green), Checking (yellow), Error (red)
- **Position:** Persistent in Privacy Dashboard header
- **Accessibility:** "Privacy protection active: 0 cloud calls blocked"

**3. FaceCircle Component**
- **Purpose:** Display person face in circular thumbnail
- **Anatomy:** Circular image + optional name label below
- **States:** Default, Named (shows name), Unnamed ("Person X")
- **Sizes:** Small (48dp), Medium (64dp), Large (80dp)
- **Interaction:** Tap → filter photos by this person
- **Accessibility:** contentDescription with person's name or "Unknown person"

**4. IndexingProgressRing Component**
- **Purpose:** Show indexing progress during initial library scan
- **Anatomy:** Circular progress ring + percentage + "Indexing photos..." label
- **States:** Indexing (animated), Paused (battery < 20%), Complete (checkmark)
- **Content:** "X of Y photos indexed"
- **Accessibility:** "Indexing in progress: X percent complete"

**5. SearchResultCard Component**
- **Purpose:** Display search result with relevance indicator
- **Anatomy:** Photo thumbnail + relevance bar + matched tags
- **States:** Default, Hovered, Selected
- **Content:** Photo preview + confidence indicator
- **Accessibility:** "Search result: [photo description], [confidence]% match"

**6. AlbumCard Component**
- **Purpose:** Display AI-curated album with cover + title
- **Anatomy:** Cover image grid (2x2) + album title + photo count
- **States:** Default, Empty ("No photos yet")
- **Variants:** People album (face circles), Location album (map pin), Event album (calendar)
- **Interaction:** Tap → open album
- **Accessibility:** "Album: [name], [count] photos"

**7. VideoThumbnail Component**
- **Purpose:** Display video with duration overlay
- **Anatomy:** Video frame + play icon + duration badge
- **States:** Default, Playing, Loading
- **Interaction:** Tap → play video inline
- **Accessibility:** "Video, [duration]"

**8. FullScreenPhotoViewer Component**
- **Purpose:** Display photo in full screen with gestures
- **Anatomy:** Full screen image + gesture handlers (pinch zoom, pan)
- **States:** Default, Zoomed, Sharing
- **Controls:** Back, Share, Info overlay
- **Interaction:** Swipe left/right → next/prev photo
- **Accessibility:** "Photo [index] of [total], [date]"

### Component Implementation Strategy

**Foundation (Material 3):** Sử dụng nguyên components có sẵn như BottomNavigation, SearchBar, Button, Card, Dialog

**Custom Components:** Xây dựng 8 custom components trên nền Material 3:
- PhotoGrid: LazyVerticalGrid với custom item composable
- PrivacyBadge: Row của Icon + Text với animated counter
- FaceCircle: Box với clip(floating(Shape.Circle)) + Text
- IndexingProgressRing: Box với CircularProgressIndicator + percentage
- SearchResultCard: Card với custom content layout
- AlbumCard: Card với cover grid + metadata
- VideoThumbnail: Box với overlay cho duration/play icon
- FullScreenPhotoViewer: Full screen box với GestureDetector

**Implementation Approach:**
1. Build custom components inherit Material 3 theming tokens
2. All components follow 8dp grid spacing system
3. Support both light/dark theme via Material 3 color scheme
4. Ensure minimum 48dp touch targets (NFR A2)
5. All interactive elements have contentDescription

### Implementation Roadmap

**Phase 1 - Core Components (MVP):**
- PhotoGrid - Grid view là core interaction
- PrivacyBadge - Privacy là key differentiator
- IndexingProgressRing - Background indexing UX
- FullScreenPhotoViewer - Core viewing experience
- SearchResultCard - Search results display

**Phase 2 - Organization Components:**
- AlbumCard - Albums tab display
- FaceCircle - People browsing
- VideoThumbnail - Video grid support

**Phase 3 - Enhancement Components:**
- Grid density variants - User preference settings
- Advanced gesture support - Pinch zoom refinement
- Share sheet integration - External app sharing

---

## UX Consistency Patterns

### Button Hierarchy

**Primary Actions (Filled Button - Material 3 FilledButton)**
- **Usage:** Main CTA - "Search", "Share", "Open Album"
- **Visual:** Filled with Primary color (#1A73E8), white text
- **Touch Target:** Min 48dp height, full width on mobile

**Secondary Actions (Outlined Button)**
- **Usage:** Less critical actions - "Cancel", "Skip", "Settings"
- **Visual:** Outlined with Primary color border, primary text

**Tertiary Actions (Text Button)**
- **Usage:** Links, auxiliary actions - "Learn More", "Privacy Policy"
- **Visual:** Text only, Primary color

**Icon Buttons**
- **Usage:** Toolbar actions - back, share, info, close
- **Visual:** 48dp touch target, icon only with contentDescription
- **Positioning:** TopAppBar for screen actions, BottomSheet for sheet actions

### Feedback Patterns

**Success Feedback**
- **Component:** Snackbar with check icon + message
- **Duration:** 3 seconds auto-dismiss
- **Example:** "Photo shared successfully"

**Error Feedback**
- **Component:** Snackbar with error color (#EA4335) + error icon + message
- **Action:** "Retry" button for recoverable errors
- **Duration:** Persistent until dismissed for critical errors

**Loading Feedback**
- **Component:** CircularProgressIndicator (spinning) or LinearProgressIndicator (determinate)
- **Usage:** Indexing shows CircularProgressIndicator with percentage
- **Placement:** Center of content area or TopAppBar trailing

**Empty States**
- **Component:** Center-aligned Column with illustration + headline + body + optional CTA
- **Example:** "No photos yet" - folder icon + "Your photo library is empty" + "Grant permission to get started"

### Form Patterns

**Text Input (Search)**
- **Component:** Material 3 SearchBar with_outlined
- **States:** Default, Focused (blue border), Loading (trailing spinner), Error (red border + message)
- **Interaction:** Type → debounce 300ms → submit → show results

**Permission Request**
- **Pattern:** Full-screen or Dialog with clear heading + explanation + single CTA
- **Timing:** Request after user taps first action requiring permission
- **Rejection:** Show explanation and "Open Settings" button

### Navigation Patterns

**Bottom Tab Navigation**
- **Tabs:** Gallery | Search | Albums | Settings
- **Style:** Material 3 NavigationBar with icons + labels
- **Indicator:** Active tab has primary color + indicator pill
- **Badge:** Settings tab shows badge for indexing status if pending

**Screen Stack Navigation**
- **Pattern:** Back arrow in TopAppBar returns to previous screen
- **Gesture:** System back gesture supported
- **Transitions:** Shared element for photo thumbnail → full screen

**Modal Navigation**
- **Pattern:** BottomSheet for sharing, filtering, quick actions
- **Gesture:** Swipe down to dismiss
- **Backdrop:** Scrim with 32% black opacity

### Search Patterns

**Search Bar Placement**
- **Position:** Persistent at top of Gallery and Search tabs
- **Style:** Material 3 SearchBar with rounded corners (28dp)
- **Placeholder:** "Search by describing what you remember..."

**Search Flow**
1. Tap search bar → keyboard appears, placeholder changes to ""
2. Type query → debounced semantic analysis (300ms)
3. Submit → loading state → results appear within 3s (NFR P1)
4. Results show with relevance indicator (colored bar)

**Search Results**
- **Layout:** Vertical list of SearchResultCard
- **Sorting:** Highest relevance first
- **Relevance Indicator:** Green bar width = match confidence (0-100%)

### Photo & Video Patterns

**Grid Display**
- **Columns:** 3 columns (small phone), 4 columns (large phone/tablet)
- **Gap:** 2dp between items
- **Aspect Ratio:** 1:1 square thumbnails
- **Loading:** Skeleton placeholder with shimmer

**Full Screen Viewing**
- **Gesture:** Tap to show/hide controls, pinch to zoom, swipe to navigate
- **Controls:** Back (top-left), Share (top-right), Info (bottom)
- **Transition:** 300ms shared element transition from thumbnail

**Video Playback**
- **Inline:** Tap thumbnail → play in grid with mute toggle
- **Full Screen:** Tap again → full screen with system controls

### Privacy Patterns

**Privacy Badge (Persistent)**
- **Position:** Privacy Dashboard header, always visible
- **Display:** Shield icon + "0 Cloud Calls" counter
- **Animation:** Counter increments with satisfying tick animation
- **Color:** Green (#34A853) when active

**Indexing Status**
- **Progress Ring:** Shown during initial indexing
- **Battery Awareness:** Amber ring + "Paused" when battery < 20%
- **Completion:** Green checkmark + "Library indexed"

---

## Responsive Design & Accessibility

### Responsive Strategy

**Platform Focus:** Mobile-first (Android phone primary)

| Device | Strategy | Layout |
|--------|----------|--------|
| **Phone (< 600dp)** | Touch-optimized, thumb-reach zones | 3-column grid, bottom nav |
| **Large Phone (600-840dp)** | Same layout, larger touch targets | 4-column grid |
| **Tablet (840dp+)** | Side navigation consideration | 4-6 column grid, sidebar |

**Multi-Window Support:**
- Split-screen during search typing
- Resizable window support for Android 7+

### Breakpoint Strategy

| Breakpoint | Width | Grid Columns | Key Adjustments |
|------------|-------|---------------|-----------------|
| **Compact** | < 600dp | 3 columns | Bottom nav, single column lists |
| **Medium** | 600-840dp | 4 columns | Larger thumbnails, same nav |
| **Expanded** | > 840dp | 5-6 columns | Consider side rail navigation |

**Window Size Classes (Material 3):**
- Compact: Phones portrait
- Medium: Phones landscape, small tablets
- Expanded: Tablets, desktop

### Accessibility Strategy

**Target:** WCAG 2.1 AA compliance

| Requirement | Implementation |
|-------------|----------------|
| **Color Contrast** | All text meets 4.5:1 (normal) / 3:1 (large) ratio |
| **Touch Targets** | Minimum 48dp for all interactive elements |
| **Font Scaling** | Support up to 200% system font size |
| **Screen Reader** | contentDescription on all images, icons, controls |
| **Motion** | Respect system reduced-motion setting |
| **Focus Indicator** | Visible 2dp outline on focused elements |

**NFR A1-A4 Compliance:**
- A1: Font scaling via sp units and ConstraintLayout
- A2: 48dp minimum touch targets enforced
- A3: High contrast mode via Material 3 color scheme
- A4: Minimal UI, simple navigation, single-task flow

### Testing Strategy

**Responsive Testing:**
- Physical devices: Xiaomi, Samsung, Pixel at various sizes
- Android Emulator covering API 29-34
- Foldable devices: Samsung Galaxy Fold, Flip testing
- Tablet: 8" and 10" layouts

**Accessibility Testing:**
- TalkBack (Android screen reader) - manual walkthrough
- Accessibility Scanner - automated checking
- Switch Access - keyboard navigation testing
- Color blindness simulation (Deuteranopia, Protanopia, Tritanopia)

**Automated Testing:**
- Espresso for UI testing
- Accessibility testing with androidx.test.espresso.accessibility
- Screenshot testing for regression detection

### Implementation Guidelines

**Responsive Development:**
```
LazyVerticalGrid(
    columns = when (localConfiguration.screenWidthDp) {
        in 0..599 -> 3
        in 600..839 -> 4
        else -> 5
    },
    horizontalArrangement = Arrangement.spacedBy(2.dp)
)
```

**Accessibility Development:**
```kotlin
// Image with contentDescription
Image(
    painter = painterResource(id = R.drawable.photo),
    contentDescription = "Photo of beach with dog, taken August 2024",
    modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
)

// IconButton with contentDescription
IconButton(
    onClick = { sharePhoto() },
    modifier = Modifier.semantics {
        contentDescription = "Share photo"
    }
) {
    Icon(Icons.Default.Share, contentDescription = null)
}
```

**Reduced Motion:**
```kotlin
val reducedMotion = androidx.compose.material3.rememberReducedMotion()
AnimatedContent(
    transitionSpec = if (reducedMotion) {
        { fadeIn() with fadeOut() }
    } else {
        { slideInHorizontally() with slideOutHorizontally() }
    }
)
```

---

<!-- UX design content will be appended sequentially through collaborative workflow steps -->
