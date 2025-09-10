# 🏃‍♂️ Fitness Tracker App

A comprehensive Android application that combines AI-powered food analysis with nutrition tracking and calendar-based meal planning. Built with modern Android development practices and clean architecture principles.

## 📱 App Concept

### Phase 1: Core Food Analysis & Nutrition Tracking
The Fitness Tracker app is designed to help users maintain a healthy lifestyle through intelligent food analysis and nutrition tracking. The app allows users to:

- **📸 Capture Food Images**: Take photos of meals or select images from gallery
- **🤖 AI-Powered Analysis**: Get detailed nutritional information using Google Gemini AI
- **📊 Nutrition Breakdown**: View comprehensive nutrition data including calories, protein, carbs, and fats
- **📅 Calendar Integration**: Track daily nutrition with a beautiful calendar interface
- **💾 Local Storage**: Save and manage food analysis history locally
- **🗑️ Data Management**: Delete unwanted analyses with confirmation dialogs

### Phase 2: AI Integration & Smart Features

#### Google Gemini AI Integration
The app leverages Google's Gemini AI for advanced food analysis capabilities:

- **🧠 Intelligent Food Recognition**: Gemini AI analyzes food images and identifies individual food items
- **📋 Detailed Nutritional Analysis**: Provides comprehensive nutrition breakdown for each food item
- **🏥 Health Status Assessment**: Evaluates the healthiness of food items (Excellent, Good, Moderate, Poor)
- **💡 Health Benefits & Concerns**: Lists specific health benefits and potential concerns for each food
- **⏱️ Digestion Time**: Estimates how long each food takes to digest
- **📝 Analysis Summary**: Generates detailed summaries of the overall meal analysis

#### AI Features:
- **Base64 Image Processing**: Efficient image encoding for AI analysis
- **Custom Prompts**: Tailored prompts for optimal food analysis results
- **Error Handling**: Robust error management for AI API failures
- **Real-time Analysis**: Fast and accurate food recognition and analysis

### Phase 3: Technology Stack & Architecture

#### Clean Architecture Implementation
The app follows Clean Architecture principles with clear separation of concerns:

```
📁 Domain Layer
├── 🏗️ Entities (GeminiFoodAnalysis, GeminiFoodItem, TotalNutrition)
├── 🔄 Use Cases (SaveFoodAnalysisUseCase, GeminiFoodAnalysisUseCase)
└── 📋 Repository Interfaces (FoodAnalysisRepository)

📁 Data Layer
├── 🗄️ Room Database (FitnessTrackerDatabase)
├── 📊 Entities (FoodAnalysisEntity, FoodItemEntity, TotalNutritionEntity)
├── 🔧 DAOs (FoodAnalysisDao)
├── 🏪 Repository Implementations (LocalFoodAnalysisRepository)
└── 🔄 Mappers (Entity to Domain model conversion)

📁 Presentation Layer
├── 🎨 UI Screens (HomeScreen, GeminiFoodAnalysisScreen, NutritionDetailsScreen)
├── 🧩 UI Components (FoodAnalysisCard, ImageSelectionDialog, DeleteFoodAnalysisDialog)
├── 🎯 ViewModels (HomeViewModel, GeminiFoodAnalysisViewModel)
└── 🧭 Navigation (NavGraph, Route definitions)
```

#### Technology Stack

**🖥️ Core Technologies:**
- **Kotlin**: Modern Android development language
- **Jetpack Compose**: Declarative UI framework
- **Material 3**: Modern design system
- **Android SDK 24+**: Support for modern Android versions

**🏗️ Architecture & Patterns:**
- **Clean Architecture**: Layered architecture with clear boundaries
- **MVVM Pattern**: Model-View-ViewModel for UI logic separation
- **Repository Pattern**: Data access abstraction
- **Use Case Pattern**: Business logic encapsulation
- **Dependency Injection**: Koin for clean dependency management

**🗄️ Data Management:**
- **Room Database**: Local SQLite database with type safety
- **Flow & StateFlow**: Reactive data streams
- **Coroutines**: Asynchronous programming
- **Base64 Encoding**: Efficient image storage

**🌐 Networking:**
- **Ktor Client**: HTTP client for API calls
- **OkHttp**: Underlying HTTP engine
- **JSON Serialization**: Kotlinx Serialization for data parsing
- **Retrofit**: REST API client (for future integrations)

**🎨 UI/UX:**
- **Jetpack Compose**: Modern declarative UI
- **Material 3**: Google's latest design system
- **Compose Navigation**: Type-safe navigation
- **Coil**: Image loading library
- **Preview Functions**: Comprehensive UI testing

**🔧 Development Tools:**
- **Koin**: Dependency injection framework
- **Gradle KTS**: Kotlin DSL for build scripts
- **Android Studio**: IDE with Compose support
- **Git**: Version control

## 📁 Project Structure

```
app/
├── src/main/java/com/bmqa/brac/fitnesstracker/
│   ├── 🏗️ common/
│   │   └── utils/JsonUtils.kt
│   ├── 🗄️ data/
│   │   ├── local/
│   │   │   ├── database/
│   │   │   │   ├── FitnessTrackerDatabase.kt
│   │   │   │   ├── entities/
│   │   │   │   │   ├── FoodAnalysisEntity.kt
│   │   │   │   │   ├── FoodItemEntity.kt
│   │   │   │   │   └── TotalNutritionEntity.kt
│   │   │   │   ├── dao/FoodAnalysisDao.kt
│   │   │   │   └── converters/
│   │   │   │       ├── HealthStatusConverter.kt
│   │   │   │       └── StringListConverter.kt
│   │   │   └── repository/LocalFoodAnalysisRepository.kt
│   │   └── remote/
│   │       └── api/GeminiApiService.kt
│   ├── 🎯 domain/
│   │   ├── entities/
│   │   │   ├── GeminiFoodAnalysis.kt
│   │   │   ├── GeminiFoodItem.kt
│   │   │   └── TotalNutrition.kt
│   │   ├── repository/FoodAnalysisRepository.kt
│   │   └── usecase/
│   │       ├── GeminiFoodAnalysisUseCase.kt
│   │       └── SaveFoodAnalysisUseCase.kt
│   ├── 🎨 presentation/
│   │   ├── features/
│   │   │   ├── home/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── DeleteFoodAnalysisDialog.kt
│   │   │   │   │   │   ├── FoodAnalysisCard.kt
│   │   │   │   │   │   └── ImageSelectionDialog.kt
│   │   │   │   │   └── screens/HomeScreen.kt
│   │   │   │   └── viewmodel/HomeViewModel.kt
│   │   │   ├── foodanalysis/
│   │   │   │   ├── ui/screens/GeminiFoodAnalysisScreen.kt
│   │   │   │   └── viewmodel/GeminiFoodAnalysisViewModel.kt
│   │   │   └── nutrition/
│   │   │       └── ui/screens/NutritionDetailsScreen.kt
│   │   ├── components/
│   │   │   ├── AppBar.kt
│   │   │   ├── DeleteFoodAnalysisDialog.kt
│   │   │   ├── FoodAnalysisCard.kt
│   │   │   ├── ImagePicker.kt
│   │   │   └── ImageSelectionDialog.kt
│   │   └── navigation/
│   │       ├── NavGraph.kt
│   │       ├── NavigationExtensions.kt
│   │       └── Route.kt
│   └── 🔧 di/
│       ├── NetworkModule.kt
│       └── ViewModelModule.kt
└── src/test/java/com/bmqa/brac/fitnesstracker/
    └── data/local/repository/LocalFoodAnalysisRepositoryTest.kt
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Hedgehog or later
- **Android SDK**: 24+ (Android 7.0+)
- **Kotlin**: 1.9+
- **JDK**: 11 or later

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/shimanto7710/fitness-tracker.git
   cd fitness-tracker
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Open the project folder
   - Wait for Gradle sync to complete

3. **Configure API Keys**:
   - Add your Google Gemini API key to `local.properties`:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```

4. **Build and Run**:
   ```bash
   ./gradlew assembleDebug
   ```

### Running Tests
```bash
./gradlew test
```

## 🎯 Key Features

### 🏠 Home Screen
- **📅 Calendar View**: Interactive calendar for date selection
- **📊 Nutrition Summary**: Daily calorie and macro tracking
- **🍎 Food Analysis List**: View saved food analyses for selected date
- **➕ Add Food**: Floating action button for new food analysis
- **🗑️ Delete Management**: Long-press to delete analyses with confirmation

### 🤖 AI Food Analysis
- **📸 Image Capture**: Camera or gallery selection
- **🧠 Gemini AI Analysis**: Intelligent food recognition and nutrition breakdown
- **📋 Detailed Results**: Comprehensive nutrition information
- **💾 Auto-Save**: Automatically saves successful analyses
- **🔄 Retry Mechanism**: Easy retry for failed analyses

### 📊 Nutrition Details
- **📈 Health Score**: Visual health assessment
- **🥗 Food Items**: Individual food item breakdown
- **📊 Total Nutrition**: Complete meal nutrition summary
- **💡 Health Insights**: Benefits and concerns for each food

## 🔧 Development

### Architecture Benefits
- **🧪 Testable**: Clear separation allows easy unit testing
- **🔄 Maintainable**: Modular structure for easy updates
- **📈 Scalable**: Easy to add new features
- **🛡️ Robust**: Proper error handling and state management

### Code Quality
- **📝 Documentation**: Comprehensive KDoc comments
- **🎨 Previews**: Extensive Compose preview functions
- **⚡ Performance**: Optimized with Flow and Coroutines
- **🛡️ Error Handling**: Comprehensive error states and recovery

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📞 Support

For support, email support@fitnesstracker.com or create an issue in the repository.

---

**Built with ❤️ using Clean Architecture, Jetpack Compose, and Google Gemini AI**