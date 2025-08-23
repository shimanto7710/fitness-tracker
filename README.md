# Fitness Tracker

This Android application includes comprehensive food recognition and calorie tracking features using **on-device Machine Learning** for image-based food analysis. Users can take photos of food and get detailed nutrition information including food names and calories.

## 🚀 Features

- **Image-based Food Recognition**: Take photos of food items for instant analysis
- **On-device ML Processing**: Privacy-first approach using TensorFlow Lite
- **Nutritional Information**: Get detailed calorie and macro breakdown
- **Offline Capability**: ML model works without internet connection
- **Modern UI**: Beautiful Material 3 design with Compose
- **Real-time Analysis**: Instant food recognition and nutritional data

## 🏗️ Architecture

The app uses a modern MVVM architecture with:

- **Jetpack Compose**: Modern declarative UI framework
- **TensorFlow Lite**: On-device machine learning for food recognition
- **Nutritionix API**: Comprehensive nutritional database
- **Repository Pattern**: Clean separation of concerns
- **Coroutines**: Asynchronous programming with Kotlin

## 🔧 Setup Requirements

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- Kotlin 1.9.22+
- JDK 11+

### Dependencies
- **TensorFlow Lite**: For on-device ML processing
- **Retrofit**: HTTP client for API calls
- **Coil**: Image loading and caching
- **Coroutines**: Asynchronous operations

## 📱 Key Components

- **FoodRecognitionMLService**: Handles TensorFlow Lite model inference
- **FoodAnalysisRepository**: Coordinates ML recognition with API calls
- **FoodAnalysisViewModel**: Manages UI state and business logic
- **FoodAnalysisScreen**: Displays recognition results and nutritional data

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

3. **Setup ML Model**
   - Download Food-101 TensorFlow Lite model
   - Place in `app/src/main/assets/` directory
   - See `FOOD_101_ML_SETUP.md` for detailed instructions

4. **Setup Nutritionix API**
   - Sign up at [nutritionix.com](https://www.nutritionix.com/business/api)
   - Get your App ID and App Key
   - Update credentials in `NutritionixService.kt`

5. **Build and Run**
   - Sync project with Gradle files
   - Build the project
   - Run on device or emulator

## 🔍 How It Works

1. **Image Selection**: User selects food image from gallery or camera
2. **ML Processing**: TensorFlow Lite model analyzes image for food recognition
3. **API Integration**: Nutritionix API provides nutritional information
4. **Results Display**: Beautiful UI shows calories, protein, carbs, and fat

## 📁 Project Structure

```
app/src/main/java/com/bmqa/brac/fitnesstracker/
├── features/
│   └── imagepicker/
│       ├── data/repositories/
│       │   ├── FoodRecognitionMLService.kt      # ML model service
│       │   └── FoodAnalysisRepository.kt        # Main repository
│       ├── presentation/
│       │   ├── viewmodels/
│       │   │   └── FoodAnalysisViewModel.kt     # ViewModel
│       │   └── screens/
│       │       └── FoodAnalysisScreen.kt        # Results UI
│       └── CaloriesManagementScreen.kt          # Main screen
├── data/
│   └── api/
│       └── NutritionixService.kt                # Nutrition API
├── di/
│   └── NetworkModule.kt                         # Dependency injection
└── MainActivity.kt                              # App entry point
```

## 🎯 Benefits

- **Privacy First**: Images never leave your device
- **Instant Results**: No waiting for network calls
- **Offline Capable**: ML model works without internet
- **Cost Effective**: No per-image API charges for recognition
- **Real-time**: Immediate food recognition and analysis

## 🔮 Future Enhancements

- Custom model training on specific food types
- Offline nutritional database
- User feedback for model improvement
- Multi-language support
- Barcode scanning integration
- Meal planning and tracking

## 📚 Documentation

- `FOOD_101_ML_SETUP.md`: Detailed ML model setup guide
- `README_ML_APPROACH.md`: Technical architecture overview
- `PACKAGE_STRUCTURE.md`: Package organization details

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:
- Check the setup guides in the documentation
- Review the troubleshooting sections
- Open an issue on GitHub

---

**Built with ❤️ using modern Android development practices**