# Room Database Implementation for Food Analysis

This document explains how to use the Room database implementation for storing Gemini food analysis responses with images.

## 🏗️ Database Structure

### Entities

1. **FoodAnalysisEntity** - Main table storing the complete analysis
   - `id` (Primary Key)
   - `isError`, `errorMessage`
   - `analysisSummary`, `dateNTime`
   - `imagePath` (path to stored image)
   - `imageUri` (original URI)
   - `createdAt` (timestamp)

2. **FoodItemEntity** - Individual food items in the analysis
   - `id` (Primary Key)
   - `analysisId` (Foreign Key to FoodAnalysisEntity)
   - `name`, `portion`, `digestionTime`
   - `healthStatus`, `calories`, `protein`, `carbs`, `fat`
   - `healthBenefits`, `healthConcerns`, `analysisSummary`

3. **TotalNutritionEntity** - Overall nutrition summary
   - `id` (Primary Key)
   - `analysisId` (Foreign Key to FoodAnalysisEntity)
   - `name`, `totalPortion`
   - `totalCalories`, `totalProtein`, `totalCarbs`, `totalFat`
   - `overallHealthStatus`

## 🔧 Usage

### 1. Initialize Repository

```kotlin
val repository = LocalFoodAnalysisRepository(context)
```

### 2. Save Food Analysis with Image

```kotlin
suspend fun saveAnalysis(foodAnalysis: GeminiFoodAnalysis, imageBitmap: Bitmap, imageUri: String) {
    val analysisId = repository.saveFoodAnalysis(
        foodAnalysis = foodAnalysis,
        imageUri = imageUri,
        imageBitmap = imageBitmap
    )
    // analysisId is the unique identifier for this analysis
}
```

### 3. Get All Analyses

```kotlin
fun getAllAnalyses(): Flow<List<GeminiFoodAnalysis>> {
    return repository.getAllFoodAnalyses()
}
```

### 4. Get Recent Analyses (for Recently Used List)

```kotlin
fun getRecentAnalyses(): Flow<List<GeminiFoodAnalysis>> {
    return repository.getRecentFoodAnalyses(limit = 10)
}
```

### 5. Get Specific Analysis by ID

```kotlin
suspend fun getAnalysisById(id: Long): GeminiFoodAnalysis? {
    return repository.getFoodAnalysisById(id)
}
```

### 6. Get Image from Stored Analysis

```kotlin
suspend fun getImage(analysisId: Long): Bitmap? {
    val analysis = repository.getFoodAnalysisById(analysisId)
    return analysis?.let { 
        repository.getImageBitmap(analysis.imagePath)
    }
}
```

### 7. Delete Analysis

```kotlin
suspend fun deleteAnalysis(id: Long) {
    repository.deleteFoodAnalysis(id)
}
```

## 📱 Integration with UI

### Recently Used List

```kotlin
@Composable
fun RecentlyUsedList() {
    val repository = LocalFoodAnalysisRepository(LocalContext.current)
    val recentAnalyses by repository.getRecentFoodAnalyses(limit = 5)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(recentAnalyses) { analysis ->
            RecentlyUsedItemCard(
                name = analysis.totalNutrition?.name ?: "Unknown Meal",
                calories = analysis.totalNutrition?.totalCalories?.toString() ?: "N/A",
                imagePath = analysis.imagePath
            )
        }
    }
}
```

### Calendar Screen Integration

```kotlin
@Composable
fun CalendarScreen() {
    val repository = LocalFoodAnalysisRepository(LocalContext.current)
    val allAnalyses by repository.getAllFoodAnalyses()
        .collectAsState(initial = emptyList())

    // Filter analyses by selected date
    val selectedDateAnalyses = allAnalyses.filter { analysis ->
        // Parse date and compare with selected date
        // Implementation depends on your date format
    }
}
```

## 🗂️ File Structure

```
data/local/
├── database/
│   ├── entities/
│   │   ├── FoodAnalysisEntity.kt
│   │   ├── FoodItemEntity.kt
│   │   └── TotalNutritionEntity.kt
│   ├── dao/
│   │   └── FoodAnalysisDao.kt
│   ├── converters/
│   │   ├── HealthStatusConverter.kt
│   │   └── StringListConverter.kt
│   └── FitnessTrackerDatabase.kt
├── repository/
│   └── LocalFoodAnalysisRepository.kt
├── model/
│   └── StoredFoodAnalysis.kt
└── example/
    └── FoodAnalysisDatabaseUsage.kt
```

## 🔄 Data Flow

1. **Save**: Gemini response → Repository → Database (with image storage)
2. **Retrieve**: Database → Repository → Domain models → UI
3. **Delete**: UI → Repository → Database (with image cleanup)

## 🖼️ Image Storage

- Images are stored in the app's internal storage
- Path format: `images/food_analysis_{timestamp}.jpg`
- Automatic cleanup when analysis is deleted
- JPEG compression at 90% quality

## ⚡ Performance Features

- **Lazy Loading**: Uses Flow for reactive data
- **Foreign Keys**: Proper relationships with CASCADE delete
- **Indices**: Optimized queries on analysisId
- **Transactions**: Atomic operations for complex data
- **Type Converters**: Efficient serialization of complex types

## 🧪 Testing

The database can be tested using the example class:

```kotlin
val usage = FoodAnalysisDatabaseUsage(context)
usage.saveExampleFoodAnalysis(bitmap, uri)
usage.getAllFoodAnalyses()
```

## 🔧 Dependencies Added

```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
```

## 📝 Notes

- Database version is 1 (increment for schema changes)
- Uses `fallbackToDestructiveMigration()` for development
- All operations are suspend functions for coroutine support
- Images are automatically managed (save/delete)
- Supports both Flow and suspend function patterns

