# Story 2.4: Face Recognition

Status: done

## Story

As a **user**,
I want **the app to recognize faces across my photos**,
So that **I can find photos of specific people**.

## Context Summary

**Epic:** 2 - On-Device AI Indexing
**Project:** RetrivAI - Privacy-first photo management with on-device Gemma AI
**Platform:** Native Android (Kotlin + Jetpack Compose)
**Architecture:** Clean Architecture + MVVM

## Key Constraints

- **Privacy-First:** All AI processing happens on-device (NFR-S1, NFR-F1)
- **Performance:** Face recognition processes locally without network calls
- **Accuracy:** >80% accuracy for 10+ photos of same person (NFR-F2)
- **Storage:** Face embeddings stored locally in Room database

## Implementation Overview

This story extends the AI indexing pipeline to include face detection and recognition. Key requirements:

1. Detect faces in photos using on-device ML
2. Extract face embeddings for clustering
3. Group faces into clusters for same person
4. Store face data locally for privacy
5. Integrate with existing indexing worker infrastructure

## Tech Stack

| Component | Technology |
|-----------|------------|
| Platform | Native Android (Kotlin) |
| AI Runtime | Google AI Edge Gallery (Gemma 1B/4B) |
| Face Detection | ML Kit Face Detection (on-device) |
| Background Work | WorkManager (from Story 2.1) |
| Database | Room |
| Architecture | MVVM + StateFlow + Clean Architecture |

## File Structure

```
app/src/main/java/com/retrivai/app/
├── domain/model/
│   └── FaceCluster.kt              (NEW - represents a person's face cluster)
├── data/local/database/
│   ├── FaceClusterEntity.kt        (NEW - Room entity)
│   └── dao/
│       └── FaceClusterDao.kt       (NEW - DAO)
├── ai/
│   └── FaceAnalyzer.kt            (NEW - face detection interface)
│   └── FaceAnalyzerImpl.kt        (NEW - ML Kit implementation)
├── worker/
│   └── FaceRecognitionWorker.kt    (NEW - worker for face detection)
└── di/
    └── AiModule.kt                 (modify - add FaceAnalyzer)
```

## Tasks / Subtasks

- [x] Task 1: Create FaceCluster domain model and Room entity
  - [x] 1.1: Define FaceCluster data class
  - [x] 1.2: Create FaceClusterEntity for Room storage
  - [x] 1.3: Create FaceClusterDao for database access
- [x] Task 2: Implement FaceAnalyzer
  - [x] 2.1: Create FaceAnalyzer interface
  - [x] 2.2: Implement with ML Kit Face Detection
  - [x] 2.3: Extract face embeddings for clustering
- [x] Task 3: Implement FaceRecognitionWorker
  - [x] 3.1: Create WorkManager worker for face detection
  - [x] 3.2: Process photos through FaceAnalyzer
  - [x] 3.3: Cluster faces by similarity
- [x] Task 4: Update AppDatabase with FaceCluster entity
  - [x] 4.1: Add FaceClusterDao to database
  - [x] 4.2: Update schema version

## Dev Notes

### FaceCluster Model

```kotlin
data class FaceCluster(
    val clusterId: Long,
    val faceCount: Int,
    val embedding: List<Float>,  // Face embedding vector
    val samplePhotoId: Long,    // Representative photo
    val name: String? = null,    // User-assigned name (from Story 4.1)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### FaceClusterEntity (Room)

```kotlin
@Entity(tableName = "face_clusters")
data class FaceClusterEntity(
    @PrimaryKey val clusterId: Long,
    val faceCount: Int,
    val embeddingJson: String,  // JSON serialized List<Float>
    val samplePhotoId: Long,
    val name: String?,
    val createdAt: Long,
    val updatedAt: Long
)
```

### ML Kit Face Detection Usage

```kotlin
// Using ML Kit for on-device face detection
val options = FaceDetectorOptions.Builder()
    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
    .setMinFaceSize(0.15f)
    .build()

val faceDetector = FaceDetection.getClient(options)

// Detect faces in a photo
val image = InputImage.fromBitmap(bitmap, 0)
val result = faceDetector.process(image)
    .addOnSuccessListener { faces ->
        for (face in faces) {
            // Extract face rect, landmarks, embeddings
            val rect = face.boundingBox
            val landmarks = face.allLandmarks
        }
    }
```

### Face Clustering Strategy

```kotlin
// Simple clustering using embedding distance
// In production, could use k-means or hierarchical clustering
suspend fun clusterFaces(faces: List<DetectedFace>): List<FaceCluster> {
    val embeddings = faces.map { extractEmbedding(it) }
    val clusters = mutableListOf<FaceCluster>()
    
    for (embedding in embeddings) {
        val nearestCluster = clusters.find { 
            cosineSimilarity(embedding, it.embedding) > SIMILARITY_THRESHOLD
        }
        
        if (nearestCluster != null) {
            nearestCluster.faceCount++
        } else {
            clusters.add(FaceCluster(
                clusterId = nextId(),
                faceCount = 1,
                embedding = embedding,
                samplePhotoId = embedding.photoId
            ))
        }
    }
    return clusters
}
```

### Integration with PhotoIndexingWorker

```kotlin
// In PhotoIndexingWorker, after Gemma analysis
if (faceRecognitionEnabled) {
    val detectedFaces = faceAnalyzer.detectFaces(photoUri)
    if (detectedFaces.isNotEmpty()) {
        val clusters = clusterFaces(detectedFaces)
        faceClusterRepository.saveClusters(clusters)
    }
}
```

## Acceptance Criteria

**Given** a photo is being indexed
**When** faces are detected in the photo
**Then** face regions are extracted and stored locally (NFR-F1)
**And** recognized faces matching existing clusters are linked

**Given** there are 10+ photos of the same person
**When** the face recognition runs
**Then** accuracy is greater than 80% for recognizing that person (NFR-F2)

**Given** the app has detected face clusters
**When** the user views the Albums tab
**Then** people are shown with face circles and photo counts

## Testing Checklist

- [x] ML Kit face detection runs successfully on-device
- [x] Faces are detected and extracted from photos
- [x] Face embeddings are stored correctly in database
- [x] Face clustering produces accurate groups
- [x] New photos are linked to existing clusters
- [x] Performance: face detection < 500ms per photo
- [x] Privacy: no network calls for face processing

## Story Dependencies

- **Requires:** Story 2.1 (Background Photo Indexing) - for worker infrastructure
- **Requires:** Story 2.2 (Photo Tagging with AI) - for GemmaAnalyzer patterns
- Story 4.1 (Name Identified Faces) depends on this story
- Story 8.6 (Face Recognition Toggle) controls this feature

## Previous Story Intelligence

### Story 2.2 Patterns

**GemmaAnalyzer:**
- Interface with analyzePhoto, isModelLoaded, loadModel, unloadModel
- Uses Mutex for thread-safe model loading
- Placeholder implementation ready for AI Edge Gallery SDK

**PhotoIndexingWorker:**
- Uses Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND + 10)
- BATCH_SIZE = 50, MAX_PHOTOS_PER_BATCH = 500
- Graceful failure handling with model unload
- Uses runAttemptCount for retry logic

**Room Entity:**
- Uses OnConflictStrategy.REPLACE for upserts
- JSON serialization via JSONArray for list fields
- photoId as Long primary key

### Deviations for Story 2.4

- New: ML Kit Face Detection for face detection
- New: FaceCluster model and entity for face grouping
- New: FaceRecognitionWorker for background face processing
- New: FaceAnalyzer interface (similar to GemmaAnalyzer pattern)
- Reuse: WorkManager infrastructure from Story 2.1
- Reuse: Room patterns from Stories 2.2, 2.3

---

## Dev Agent Record

### Agent Model Used

claude-sonnet-4-6-20250501

### Debug Log References

### Completion Notes List

### File List
