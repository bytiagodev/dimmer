<p align="center">
  <img src="dimmer-banner.svg" alt="Dimmer — something good is on." width="680">
</p>

> Something good is on.

Dimmer is a film and TV discovery app for Android, built for the end-of-day couch moment. Browse popular films, search by title, filter by genre or streaming service, and see exactly where to watch — all tuned to your region.

---

## Features

- Browse popular and trending films
- Search by title
- Filter by genre, decade, and streaming service
- See where to watch (Netflix, HBO Max, Disney+, Apple TV+, Prime) for Portugal
- Dynamic color extraction from film posters
- Film details: poster, rating, runtime, genres, synopsis, similar films

---

## Tech Stack

### Frontend (Android)

| What | Why |
|---|---|
| **Kotlin** | Official Android language, concise, null-safe |
| **Jetpack Compose** | Modern declarative UI toolkit, replaces XML layouts |
| **Material 3** | Google's design system with built-in theming |
| **Retrofit + Moshi** | Industry standard for HTTP + JSON on Android |
| **Coil** | Lightweight image loading built for Compose |
| **AndroidX Palette** | Color extraction from poster images |
| **Compose Navigation** | Single-activity navigation |

### Backend

| What | Why |
|---|---|
| **Kotlin** | Same language as the Android app |
| **Ktor** | Lightweight Kotlin-native server framework |

### Data

| What | Why |
|---|---|
| **TMDB API** | Free tier, high quality images, watch provider data |

---

## Architecture

```
Android App  ──HTTP──▶  Ktor Server  ──HTTP──▶  TMDB API
```

The app never talks to TMDB directly. All requests go through the Ktor backend, keeping the API key off the device.

---

## Getting Started

### Prerequisites
- Android Studio (Ladybug or newer)
- Android SDK 34
- An emulator (API 34) or physical device with USB debugging
- TMDB API key ([themoviedb.org](https://www.themoviedb.org/settings/api))

### Run the app
1. Clone the repo
   ```
   git clone https://github.com/bytiagodev/dimmer.git
   ```
2. Open the project in Android Studio
3. Wait for Gradle sync to finish
4. Set the backend base URL in `RetrofitClient.kt` (use `http://10.0.2.2:8080` for emulator pointing at localhost)
5. Run on emulator or device

### Run the backend
See the backend README in `/backend` (maintained by the backend dev).

---

## Project Structure

```
app/src/main/java/com/dimmer/app/
├── data/
│   ├── api/            Retrofit interface, data models
│   └── repository/     Repository layer
├── ui/
│   ├── home/           Home screen + ViewModel
│   ├── detail/         Detail screen + ViewModel
│   ├── search/         Search screen + ViewModel
│   ├── components/     Shared composables (FilmCard, etc.)
│   └── theme/          Colors, typography, Material 3 theme
└── navigation/         NavGraph setup
```

---

## Team

| Role | Person |
|---|---|
| Frontend (Android) | @bytiagodev |
| Backend (Ktor) | [@oliveira-p](https://github.com/oliveira-p) |

---

## Acknowledgements

- Film data provided by [TMDB](https://www.themoviedb.org/). This product uses the TMDB API but is not endorsed or certified by TMDB.
- Typography: [Playfair Display](https://fonts.google.com/specimen/Playfair+Display) and [DM Sans](https://fonts.google.com/specimen/DM+Sans) via Google Fonts.
