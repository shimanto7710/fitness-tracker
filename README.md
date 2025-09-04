# Food Analysis App

A comprehensive food analysis application with AI-powered nutritional information capabilities.

## Features

- **AI Food Analysis**: Uses OpenRouter API to analyze food images and provide nutritional information
- **Image Capture**: Take photos or select from gallery for food analysis
- **Network Profiling**: Built-in OkHttp Profiler for debugging API calls

## Network Debugging with OkHttp Profiler

The app includes OkHttp Profiler integration to help debug and monitor network calls to the OpenRouter API.

### How to Use OkHttp Profiler

1. **Install the OkHttp Profiler Plugin** (if not already installed):
   - In Android Studio, go to `File` → `Settings` → `Plugins`
   - Search for "OkHttp Profiler" and install it
   - Restart Android Studio

2. **View Network Calls**:
   - Run the app in debug mode
   - Navigate to the "AI Food Analysis" screen
   - Take a photo or select an image
   - Click "Analyze Food Image"
   - In Android Studio, look for the "OkHttp Profiler" tab at the bottom
   - You'll see detailed information about the OpenRouter API call including:
     - Request headers and body
     - Response headers and body
     - Timing information
     - Network errors (if any)

3. **What You'll See**:
   - **Request Details**: Full HTTP request with headers, body, and URL
   - **Response Details**: Complete API response from OpenRouter
   - **Timing**: Request duration and network performance metrics
   - **Headers**: All request and response headers including authentication

### Network Configuration

The app uses separate OkHttp clients for different APIs:
- **OpenRouter API**: For AI food analysis
- **Custom Food Detection API**: For additional food detection features

Both clients include:
- HTTP logging interceptor (for console logs)
- OkHttp Profiler interceptor (for debugging)
- Custom authentication interceptors
- Configurable timeouts

### Example API Call Flow

```
1. User selects/captures image
2. Image converted to base64
3. OpenRouter API request sent with:
   - Model: qwen/qwen-2.5-vl-7b-instruct
   - Image data in base64 format
   - Custom prompt for food analysis
4. Response processed and displayed in UI
5. All network activity logged and profiled
```

## Development

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- Kotlin 1.9+

### Building
```bash
./gradlew assembleDebug
```

### Running Tests
```bash
./gradlew test
```

## Architecture

The app follows Clean Architecture principles with:
- **Presentation Layer**: Compose UI with ViewModels
- **Domain Layer**: Use cases and repository interfaces
- **Data Layer**: Repository implementations and data sources
- **Network Layer**: Retrofit with OkHttp and interceptors

## Dependencies

- **UI**: Jetpack Compose, Material 3
- **Networking**: Retrofit, OkHttp, OkHttp Profiler
- **Dependency Injection**: Dagger Hilt
- **Image Loading**: Coil
- **Async Operations**: Kotlin Coroutines
