This is a **Clean Architecture Android app** that demonstrates modern Android development practices with clean architecture principles. <cite/>

## Tech Stack

**Frontend & UI:**
- **Jetpack Compose** - Modern declarative UI framework with Material Design 3 [1](#0-0) 
- **Kotlin** - Primary development language targeting JVM 11 [2](#0-1) 

**Architecture & DI:**
- **Clean Architecture** - Separation of concerns across presentation, domain, and data layers <cite/>
- **Hilt** - Dependency injection framework [3](#0-2) 
- **MVVM Pattern** - ViewModels with reactive state management using StateFlow [4](#0-3) 

**Networking & Data:**
- **Retrofit + OkHttp** - REST API communication with the REST Countries API [5](#0-4) 
- **Room Database** - Local SQLite persistence for favorites [6](#0-5) 
- **Kotlin Flow** - Reactive programming for asynchronous data streams [7](#0-6) 

**Additional Libraries:**
- **Coil** - Async image loading with SVG support for country flags [8](#0-7) 
- **Compose Navigation** - Type-safe screen routing [9](#0-8) 

## Architecture

The app follows **Clean Architecture** with three distinct layers:

**Presentation Layer:** Jetpack Compose screens (`HomeScreen`, `CountryDetailScreen`, `FavoritesScreen`) with corresponding ViewModels managing UI state [10](#0-9) 

**Domain Layer:** Business logic with use cases (`GetAllCountriesUseCase`, `AddToFavoritesUseCase`) and domain models like `Country` [11](#0-10) 

**Data Layer:** Repository pattern coordinating between remote API service and local Room database [12](#0-11) 

## What the App Does

A **country information browser** that fetches data from the REST Countries API and allows users to:

- **Browse all countries** with search functionality in a clean list interface [13](#0-12) 
- **View detailed country information** including population, region, languages, currencies, and flags [14](#0-13) 
- **Manage favorites** - add/remove countries for offline access with local persistence [15](#0-14) 
- **Offline support** for favorite countries through Room database storage <cite/>

The app targets Android API 29+ and demonstrates modern Android development with reactive programming, clean architecture, and Material Design 3. [16](#0-15) 

## Notes

The application uses the REST Countries API (https://restcountries.com/v3.1/) as its data source and implements proper error handling, loading states, and offline capabilities. The codebase serves as a reference implementation for clean architecture principles in Android development.

Wiki pages you might want to explore:
- [Overview (Jimmy766/clean-kotlin-android)](/wiki/Jimmy766/clean-kotlin-android#1)
- [UI Screens and ViewModels (Jimmy766/clean-kotlin-android)](/wiki/Jimmy766/clean-kotlin-android#6.2)
