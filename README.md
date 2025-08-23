# 🍎 Fitness Tracker - AI Food Recognition

A modern Android fitness tracking app that uses **Clarifai's AI** for instant food recognition and nutritional analysis.

## 🚀 Features

- **AI Food Recognition**: Take photos of food for instant analysis
- **Nutritional Information**: Get detailed calorie and macro breakdown
- **Real-time Analysis**: Instant results with confidence scores
- **Modern UI**: Beautiful Material 3 design with Jetpack Compose
- **Privacy-First**: Images processed locally before API calls

## 🏗️ Architecture

The app uses a clean, modern architecture:

- **Jetpack Compose**: Modern declarative UI framework
- **Clarifai AI**: Industry-leading food recognition API
- **Repository Pattern**: Clean separation of concerns
- **Coroutines**: Asynchronous programming with Kotlin
- **MVVM**: Model-View-ViewModel architecture

## 🔧 Setup Requirements

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- Kotlin 1.9.22+
- JDK 11+

### Dependencies
- **Clarifai API**: For AI food recognition
- **OkHttp**: HTTP client for API calls
- **Coil**: Image loading and caching
- **Coroutines**: Asynchronous operations

## 📱 Key Components

- **ClarifaiHelper**: Direct API client for food recognition
- **ClarifaiService**: Business logic and image processing
- **FoodNutritionDatabase**: Local nutrition database
- **ImagePicker**: Camera and gallery integration
- **ClarifaiFoodRecognizer**: Food analysis UI component

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FitnessTracker
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Setup Clarifai API**
   - Get your Personal Access Token (PAT) from [Clarifai](https://clarifai.com)
   - Update the PAT in `ClarifaiHelper.kt`

4. **Build and Run**
   - Sync project with Gradle files
   - Build the project
   - Run on device or emulator

## 🔍 How It Works

1. **Image Selection**: User selects food image from gallery or camera
2. **Image Processing**: Image converted to base64 format
3. **AI Analysis**: Clarifai API analyzes image for food recognition
4. **Nutrition Lookup**: Local database provides nutritional information
5. **Results Display**: Beautiful UI shows calories, protein, carbs, and fat

## 📁 Project Structure

```
app/src/main/java/com/bmqa/brac/fitnesstracker/
├── data/
│   ├── models/
│   │   ├── FoodItem.kt              # Food data model
│   │   └── FoodNutrition.kt         # Nutrition data model
│   ├── local/
│   │   └── FoodNutritionDatabase.kt # Local nutrition database
│   ├── services/
│   │   ├── ClarifaiHelper.kt        # API client
│   │   └── ClarifaiService.kt       # Business logic
│   └── network/
│       └── ClarifaiNetworkModule.kt # Network setup
├── features/
│   └── imagepicker/
│       ├── ImagePicker.kt           # Image selection
│       ├── ClarifaiFoodRecognizer.kt # Food analysis UI
│       └── CaloriesManagementScreen.kt # Main screen
├── ui/theme/                        # App theming
└── MainActivity.kt                  # App entry point
```

## 🎯 Benefits

- **AI-Powered**: Industry-leading food recognition accuracy
- **Instant Results**: Real-time analysis and nutrition data
- **Privacy-First**: Local image processing before API calls
- **Professional UI**: Modern Material 3 design
- **Comprehensive**: 20+ food categories with nutrition data

## 🔑 API Configuration

The app is configured with:
- **Clarifai Model**: `food-item-recognition`
- **API Endpoint**: `https://api.clarifai.com/v2/models/food-item-recognition/outputs`
- **Authentication**: Personal Access Token (PAT)

## 📊 Supported Foods

The app recognizes and provides nutrition for:
- **Desserts**: cake, cookie, pastry, chocolate, ice cream
- **Fruits**: apple, banana, orange, strawberry
- **Grains**: bread, rice, pasta
- **Dairy**: cheese, butter
- **Fast Food**: pizza, burger
- **And many more...**

---

**Status: Production Ready** 🚀

Your fitness tracker is now a complete, AI-powered food recognition app using Clarifai's industry-leading technology!
