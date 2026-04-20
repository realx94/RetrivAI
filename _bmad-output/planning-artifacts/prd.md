---
stepsCompleted: ["step-01-init", "step-02-discovery", "step-02b-vision", "step-02c-executive-summary", "step-03-success", "step-04-journeys", "step-05-domain-skipped", "step-06-innovation", "step-07-project-type", "step-08-scoping", "step-09-functional", "step-10-nonfunctional", "step-11-polish", "step-12-complete"]
workflowStatus: "complete"
---
inputDocuments: ["_bmad-output/brainstorming/brainstorming-session-2026-04-20-000000.md"]
workflowType: 'prd'
---

# Product Requirements Document - RetrivAI

**Author:** RxHunter
**Date:** 2026-04-20

## Executive Summary

**RetrivAI** is a privacy-first mobile photo management app that uses on-device AI (Google Gemma) to enable natural language semantic search across user's photo library.

### Problem Statement
Users with large photo collections (1,000-2,000+ photos) cannot find specific images when needed. Current solutions rely on metadata (date, location) or basic face recognition, forcing users to scroll manually through their entire library or remember machine-friendly terms rather than natural descriptions. Cloud-based AI alternatives raise privacy concerns as photos leave the device.

### Product Vision
Every photo becomes instantly findable through the way users naturally think about it — no metadata, no folders, no cloud. Users simply describe what they're looking for in plain language, and the AI understands context, content, and meaning to return relevant results.

### Target Users
- Heavy photo takers (1,000-2,000+ photos) who cannot manage their library
- Privacy-conscious users who do not want photos sent to cloud servers
- Low-tech users who find traditional album/folder organization frustrating

### What Makes This Special
**100% on-device AI** with Gemma model running locally — photos never leave the device. Adaptive model selection (Gemma 1B for 4GB+ devices, Gemma 4B for 8GB+ devices) ensures broad device compatibility while maintaining sophisticated semantic understanding. Users search by describing what they remember — "photos with my dog at the beach," "that birthday party with A and B" — and the AI understands meaning, not just metadata.

## Project Classification

- **Project Type:** Mobile App (Android-first, iOS expansion later)
- **Domain:** Photo/Media Management with Local AI
- **Complexity:** Medium
  - Novel technology: On-device Gemma integration
  - Moderate risk: Privacy-focused but not heavily regulated
- **Project Context:** Greenfield (brand new product)

## Success Criteria

### User Success
- User finds specific photo within 10 seconds using natural language search
- User experiences "aha!" moment when AI understands abstract description ("that birthday party feeling")
- User trusts app never sends photos to cloud (Privacy Dashboard visible proof)
- Zero manual album creation required — AI organizes automatically

### Business Success
- Phase 1: MVP launched on Android, 1,000+ downloads
- User retention: 60%+ weekly active users after 1 month
- App rating: 4.0+ stars on Google Play
- Proof: Users successfully find photos they couldn't before

### Technical Success
- Semantic search returns relevant results in <3 seconds on mid-range devices
- Gemma 1B runs on devices with 4GB+ RAM
- Initial indexing: 2000 photos indexed within 30 minutes
- Battery: <15% battery drain for full library indexing
- 100% offline functionality — no cloud dependency

### Measurable Outcomes
| Metric | Target |
|--------|--------|
| Search success rate | >80% relevant results |
| Search response time | <3 seconds |
| Device compatibility | Gemma 1B on 4GB+ RAM |
| Privacy | Zero network calls for AI processing |

## Product Scope

### MVP (Phase 1)
- Natural language semantic search
- AI auto-tagging of photos (on-device)
- Living Albums (auto-updating)
- Adaptive model selection (1B/4B)
- Basic photo grid view

### Growth Features (Phase 2)
- For You Photos rediscoveries
- This Week's Memories
- Privacy Dashboard
- One-tap Smart Share

### Vision (Phase 3)
- AI color editing
- Memory presentation/videos
- iOS expansion

## User Journeys

### Journey 1: Minh finds his dog's beach photo

**Persona:** Minh, 35yo office worker, low-tech, 2000+ photos

**Opening Scene:**
It's Friday evening. Minh wants to show his friend a photo of his dog at the beach from last summer vacation. He scrolls... scrolls... scrolls. 30 minutes pass. He gives up, frustrated.

**Rising Action:**
Monday, Minh downloads RetrivAI. The app asks permission to access photos. "This will index your photos on-device," it says. Minh hesitates but agrees.

**Climax:**
Wednesday, Minh opens RetrivAI. Types: "anh cho con chó o bai bien". In 2 seconds, there it is — his dog running on Vung Tau beach, summer 2024.

**Resolution:**
Minh texts his friend: "Found it! Look how cute he was!" He shows his wife. "This app is amazing, it understood what I was looking for."

---

### Journey 2: Linh never worries about privacy

**Persona:** Linh, 28yo developer, privacy-first

**Opening Scene:**
Linh refuses to use Google Photos because "my photos would be on Google's servers." She manually organizes into folders. It's tedious.

**Rising Action:**
She reads about RetrivAI. "100% on-device AI — photos never leave your device." She's skeptical. Tests by searching for "my apartment" — results show only her actual apartment photos.

**Climax:**
She opens Privacy Dashboard: "0 cloud calls made. Your photos are processed only on this device." She's impressed. "This is the first AI photo app I actually trust."

**Resolution:**
Linh recommends RetrivAI to her privacy-conscious friends. "Finally, an AI that doesn't require sending my photos to some server."

---

### Journey 3: Ba rediscovers forgotten memories

**Persona:** Ba, 45yo businessman, 10,000+ photos, never deletes

**Opening Scene:**
Ba has photos from 2018-2024 all mixed together. He knows he has great photos but the thought of finding them is overwhelming. He rarely looks at old photos.

**Rising Action:**
RetrivAI indexes his entire library over 2 hours (while charging). The next day, he sees "For You Photos" — photos from 2 years ago he forgot existed.

**Climax:**
He taps "This Week's Memories" — his daughter's 10th birthday party photos from 2023. He's moved. "I forgot these existed. These are precious moments."

**Resolution:**
Ba starts opening RetrivAI weekly. "It's like rediscovering my own life. The app knows my memories better than I do."

---

### Journey Requirements Summary

| Capability | Required By |
|------------|-------------|
| Natural language search | All users |
| On-device AI processing | Linh (privacy), all users |
| Auto-tagging/organization | Ba, Minh |
| For You Photos / rediscoveries | Ba |
| Privacy Dashboard | Linh |
| Living Albums | All users |
| Fast indexing (<30 min) | All users |

## Innovation & Novel Patterns

### Detected Innovation Areas

**1. True On-Device Gemma AI**
- Full Gemma model running locally on mobile device — not a lightweight model or cloud proxy
- Adaptive model selection (1B/4B) based on device capability
- No cloud dependency for AI inference

**2. Privacy-First AI Photo Management**
- Zero network calls for AI processing — verifiable by user
- Photos never leave device for AI analysis
- Privacy Dashboard provides visible proof of privacy

**3. Semantic Search for Personal Photos**
- Search by describing meaning and context — "my dog at the beach"
- Understanding abstract concepts not just metadata
- No need to remember file names, dates, or locations

### Market Context & Competitive Landscape

| Competitor | AI Approach | Privacy | Semantic Search |
|------------|-------------|---------|----------------|
| Google Photos | Cloud AI | Sends photos to server | Limited (objects, faces) |
| Apple Photos | On-device (iOS) | Local only on Apple | Limited to Apple ecosystem |
| Samsung Gallery | Basic AI | Cloud optional | Basic |
| **RetrivAI** | **On-device Gemma** | **100% local, verifiable** | **Full semantic understanding** |

### Validation Approach

1. **Beta Testing**: Release to 100 users with large photo libraries
2. **Success Metric**: >80% search success rate for natural language queries
3. **Privacy Validation**: Network traffic monitoring to confirm zero cloud calls
4. **User Feedback**: "Did you find the photo you were looking for?"

### Risk Mitigation

| Risk | Mitigation |
|------|------------|
| Gemma 1B quality insufficient | Offer Gemma 4B for capable devices |
| Indexing too slow | Background indexing with progress indicator |
| Battery drain | Lazy loading, charging-only full indexing |
| User adoption friction | Simple onboarding, immediate value demonstration |

## Mobile App Specific Requirements

### Platform & Technology

| Decision | Choice | Rationale |
|----------|--------|-----------|
| **Platform** | Native Android (Kotlin) | Best Gemma/AI performance, direct GPU access |
| **Gemma Integration** | Google AI Edge Gallery | Official support, handles LiteRT runtime |
| **Min SDK** | Android 10 (API 29) | AI Edge Gallery requirement, ~85% device coverage |
| **Storage** | MediaStore + Scoped Storage | Android compliant photo access |
| **Architecture** | Clean Architecture + MVVM | Maintainable, testable code |

### Key Libraries

| Category | Library |
|----------|---------|
| UI | Jetpack Compose |
| DI | Hilt |
| Async | Kotlin Coroutines + Flow |
| AI Runtime | Google AI Edge Gallery (LiteRT) |
| Image Loading | Coil |
| Database | Room (photo metadata/index) |

### Device Permissions

| Permission | Purpose |
|------------|---------|
| `READ_MEDIA_IMAGES` | Access photos on Android 13+ |
| `READ_EXTERNAL_STORAGE` | Access photos on Android 10-12 |
| `INTERNET` | Google Play (optional updates only) |
| `ACCESS_NETWORK_STATE` | Check connectivity (for updates) |

### Technical Constraints

- **Offline-First**: All AI processing must work without internet
- **Memory Management**: Gemma 1B requires 4GB RAM minimum
- **Battery**: Background indexing when device is charging
- **Storage**: Minimal app footprint, indexed data stored locally

### Android-Specific Considerations

- **Scoped Storage**: Use MediaStore API for photo access
- **Background Work**: WorkManager for photo indexing
- **Model Delivery**: Bundle Gemma 1B with app, offer 4B as download
- **GPU Acceleration**: Use GPUDelegate for faster inference

## Project Scoping & Phased Development

### MVP Strategy & Philosophy

**MVP Approach:** Problem-Solving MVP - Focus on ONE core problem: "I can't find my photos"

**Resource Requirements:**
- 1-2 developers with Android/Kotlin experience
- Basic Gemma/AI Edge integration knowledge
- Timeline: 3-4 months for MVP

### MVP Feature Set (Phase 1)

**Core User Journeys Supported:**
- Minh finding his dog's beach photo (search success)
- Linh verifying privacy (trust establishment)
- Ba browsing rediscovered photos (engagement)

**Must-Have Capabilities:**
- Natural language semantic search (core value)
- Photo grid view (basic browsing)
- On-device AI tagging via Gemma
- Basic search results display
- Adaptive model selection (1B/4B based on device)

### Post-MVP Features

**Phase 2 (Growth):**
- Living Albums (auto-updating)
- For You Photos rediscoveries
- Privacy Dashboard
- This Week's Memories
- Photo Journal (export AI descriptions as readable timeline/diary)

**Phase 3 (Expansion):**
- AI color editing
- Memory presentation/videos
- iOS expansion

### Risk Mitigation Strategy

| Risk | Mitigation |
|------|------------|
| Gemma 1B quality insufficient | User can switch to 4B on capable devices |
| Indexing too slow | Background indexing with progress indicator |
| Low user adoption | Demo mode showing value in <30 seconds |
| Battery drain during indexing | Lazy loading, charging-only full indexing |

## Functional Requirements

### 1. Photo Access & Management

- FR1: Users can grant photo library access permission
- FR2: Users can view all photos in a grid layout sorted by date
- FR3: Users can view individual photo in full screen
- FR4: Users can share photos to external apps

### 2. Video Access & Management

- FR5: Users can view videos in a grid layout sorted by date
- FR6: Users can play videos within the app
- FR7: Users can share videos to external apps
- FR8: System generates semantic descriptions for videos similar to photos

### 3. AI Semantic Search

- FR9: Users can enter natural language queries to search photos and videos
- FR10: Users can receive results matching semantic meaning of query
- FR11: Users can view search results with relevance indication
- FR12: System automatically indexes photos and videos using on-device AI

### 4. AI Photo & Video Understanding

- FR13: System generates descriptive tags for each photo
- FR14: System generates descriptive tags for each video
- FR15: System identifies people, objects, locations, and events in photos and videos
- FR16: System identifies faces and recognizes same person across photos
- FR17: Users can name identified faces (e.g., "Mom", "Best friend A")
- FR18: Users can name identified pets and objects (e.g., "my dog Lucky", "red car")
- FR19: System maintains searchable index of all descriptions and named entities

### 5. Adaptive Model Selection

- FR20: System detects device RAM capability
- FR21: System selects appropriate Gemma model (1B or 4B) based on device
- FR22: Users can manually switch between Gemma models
- FR23: Users can select which model to download for storage management

### 6. Photo & Video Organization

- FR24: System suggests albums based on photo and video content
- FR25: Users can view system-suggested albums
- FR26: System automatically groups similar photos and videos together
- FR27: Users can browse content by people, places, or events
- FR28: Users can search for photos by person's name (e.g., "photos with Mom")
- FR29: Users can search for photos by pet's name (e.g., "photos with my dog Lucky")

### 7. Privacy & On-Device Processing

- FR30: All AI processing occurs on-device without network transmission
- FR31: System provides visibility into which AI processes are running
- FR32: System never transmits user photos or videos to external servers
- FR33: Users can view Privacy Dashboard showing data processing status

### 8. Indexing & Performance

- FR34: System indexes photos and videos in background
- FR35: Users can view indexing progress
- FR36: System completes initial indexing within 30 minutes for 2000 photos
- FR37: Users can configure indexing priority (battery vs speed)

### 9. Settings & Customization

- FR38: Users can configure default search model (1B or 4B)
- FR39: Users can enable/disable auto-album suggestions
- FR40: Users can configure indexing behavior (Wi-Fi only, while charging, etc.)
- FR41: Users can customize grid view density (small, medium, large thumbnails)
- FR42: Users can configure notification preferences
- FR43: Users can clear AI index and re-index from scratch
- FR44: Users can manage named people and pets (rename, delete, merge)
- FR45: Users can enable/disable face recognition feature

## Non-Functional Requirements

### Performance

| NFR | Requirement |
|-----|-------------|
| P1 | Semantic search returns results within 3 seconds on mid-range devices |
| P2 | Initial indexing completes within 30 minutes for 2000 photos |
| P3 | App cold start time < 2 seconds |
| P4 | Smooth scrolling (60fps) in photo grid with 10,000+ items |
| P5 | Background indexing uses < 20% CPU |

### Security & Privacy

| NFR | Requirement |
|-----|-------------|
| S1 | Zero network calls for AI processing (verifiable) |
| S2 | All user data stored locally only |
| S3 | No analytics or tracking that sends data external |
| S4 | User photos never leave device for AI analysis |
| S5 | Privacy Dashboard shows real-time processing status |

### Accessibility

| NFR | Requirement |
|-----|-------------|
| A1 | Support for system font scaling |
| A2 | Touch targets minimum 48dp |
| A3 | High contrast mode support |
| A4 | Simple, minimal UI suitable for low-tech users |

### Battery & Storage

| NFR | Requirement |
|-----|-------------|
| B1 | Background indexing pauses when battery < 20% |
| B2 | App storage footprint < 100MB (excluding Gemma model) |
| B3 | Gemma 1B model size ~1.5GB, 4B model ~3-4GB |

### Face Recognition

| NFR | Requirement |
|-----|-------------|
| F1 | Face recognition processes locally on-device |
| F2 | Face recognition accuracy > 80% for 10+ photos of same person |
| F3 | Users can name faces and those names are searchable |
| F4 | Pet/object recognition identifies common pets (dog, cat) and allows naming |
