# MovieTV — Android app (Jetpack Compose, Koin, Retrofit, RxJava)

A simple Android app that lists movies and TV shows using the Watchmode API. Built with Jetpack Compose for UI, Koin for dependency injection, Retrofit + RxJava3 for networking/concurrency, and Coil for image loading.


## Overview
- Kotlin Android application targeting modern Android SDKs
- UI: Jetpack Compose + Material 3
- DI: Koin
- Networking: Retrofit, OkHttp logging interceptor, Gson
- Concurrency: RxJava3 (RxKotlin/RxAndroid)
- Image loading: Coil
- Navigation: `navigation-compose`

The app fetches lists of titles and title details from Watchmode and displays them in Compose screens.


## Tech stack
- Language: Kotlin
- Kotlin: 2.2.21
- Compose BOM: 2025.10.01
- Min SDK: 33
- Target SDK: 36
- Java/Kotlin target: Java 11 bytecode (`jvmTarget=11`)

### Key libraries (see `app/build.gradle.kts`):
- Compose Navigation
- Retrofit 3.x line and Gson converter
- OkHttp logging interceptor
- RxKotlin / RxAndroid
- Koin
- Coil (Compose)
- Shimmer: `com.valentinilk.shimmer:compose-shimmer`


## Requirements
- Android Studio latest stable
- JDK 17+ installed for Gradle/AGP (the module targets Java 11 bytecode)
- Android SDKs for compile/target SDK 36
- An Android device or emulator (API 33+ recommended)


## Getting started
1. Clone the repo
   ```bash
   git clone https://github.com/hraj9258/MovieTv
   cd MovieTV
   ```
2. Configure API key
   - Obtain a Watchmode API key (if you have access).
   - Create or edit `local.properties` in the project root and add:
     ```properties
     WATCHMODE_API_KEY=your_watchmode_api_key_here
     ```
   - The build injects this value into `BuildConfig.WATCHMODE_API_KEY` (see `app/build.gradle.kts`).
3. Open in Android Studio
   - Open the project root in Android Studio and let it sync.
   - Use the “Run” button to install/start the app on a device or emulator.

Alternatively, build via CLI:
```bash
# Assemble a debug APK
./gradlew :app:assembleDebug

# Install on a connected device/emulator
./gradlew :app:installDebug

# (Optional) Start the main activity via adb
adb shell am start -n com.hraj9258.movietv/.MainActivity
```


## Environment & configuration
- API base URL: `https://api.watchmode.com/v1/` (defined in `app/src/main/java/com/hraj9258/movietv/di/AppModules.kt`)
- API key: Provided at build time via `local.properties` as `WATCHMODE_API_KEY` and exposed as `BuildConfig.WATCHMODE_API_KEY`.
- Internet permission is declared in `AndroidManifest.xml`.

If the API key is missing or invalid, requests to Watchmode will fail. In that case, check logs (OkHttp logging interceptor is enabled via the Koin-provided client).


## How it’s organized (project structure)
```
MovieTV/
├─ app/
│  ├─ src/main/
│  │  ├─ AndroidManifest.xml
│  │  ├─ java/com/hraj9258/movietv/
│  │  │  ├─ MainApplication.kt            # Initializes Koin modules
│  │  │  ├─ MainActivity.kt               # Compose entry point, hosts navigation
│  │  │  ├─ data/
│  │  │  │  ├─ model/                     # DTOs: Title, TitleDetails, TitleResponse
│  │  │  │  └─ remote/                    # WatchmodeApiService (Retrofit interface)
│  │  │  ├─ di/                           # Koin modules: network, repository, viewModel
│  │  │  ├─ navigation/                   # AppNavigation + destinations
│  │  │  └─ ui/                           # Compose screens, theme, components
│  │  └─ res/                             # Resources (icons, strings, theme)
│  ├─ build.gradle.kts                    # Module config & dependencies
├─ build.gradle.kts                       # Root plugins (AGP, Kotlin, Compose)
├─ gradle/libs.versions.toml              # Version catalog
├─ settings.gradle.kts                    # Includes :app
└─ README.md
```

## Dependency injection
Koin modules are defined in `di/AppModules.kt`:
- `networkModule`: OkHttp client, Retrofit with base URL, Gson converter, RxJava3 call adapter, `WatchmodeApiService`.
- `repositoryModule`: provides `MainRepository`.
- `viewModelModule`: provides `HomeViewModel` and parameterized `DetailsViewModel`.


## Networking
`WatchmodeApiService` defines endpoints:
- `GET list-titles` → `getTitles(types, limit=20, apiKey)`
- `GET title/{title_id}/details` → `getTitleDetails(titleId, apiKey)`

Both default `apiKey` params use `BuildConfig.WATCHMODE_API_KEY`.

Android Studio recommended for running and debugging.
