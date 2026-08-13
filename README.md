<p align="center">
  <img src="screenshots/dimmer-readme-banner.webp" alt="Dimmer" width="800">
</p>

<br>

I originally started building Dimmer as a final project for an Android module in a course I was taking. It had a README back then, but I left out the part about it being for a class because I didn't want it to look like just another school assignment.

But the app actually solved a real annoyance for me. Finding out where a movie is streaming in Portugal can be surprisingly hard. Since it is a smaller market, searching online doesn't always give you clear answers. I just wanted a quick way to check if a film was on Netflix or Prime without digging through random forums or guessing. That is what Dimmer does.

---

## What it does

The home screen shows a list of popular and trending films. When you tap one, it opens a detail screen with the rating, runtime, genres, and a short synopsis. The most important part is the watch providers section, which shows which services have the film available in Portugal. There is also a search screen to find specific titles.
To make the UI feel a bit more tailored to each movie, I used the AndroidX Palette library to extract the dominant colors from the movie poster and apply them to the detail screen background. 

---

## Screenshots

<p float="left">
  <img src="screenshots/screenshot-home.png" width="30%" />
  <img src="screenshots/screenshot-detail.png" width="30%" />
  <img src="screenshots/screenshot-search.png" width="30%" />
</p>

---

## Tech stack

The app is written in Kotlin and uses Jetpack Compose for the UI, which let me build the screens without writing any XML. I used Material 3 for the dark theme and design system. For networking, Retrofit handles the HTTP requests and Moshi parses the JSON responses from the TMDB API. Coil handles the image loading since it is built specifically for Compose. Navigation between the home, search, and detail screens is handled by Compose Navigation.

---

## Run it locally

You'll need a free TMDB API key from [themoviedb.org](https://www.themoviedb.org/settings/api).

1. Clone the repo
   ```
   git clone https://github.com/bytiagodev/dimmer.git
   ```
2. Open in Android Studio and wait for Gradle sync
3. Open `RetrofitClient.kt` and replace `your_tmdb_api_key_here` with your actual TMDB key
4. Run on an emulator or physical device

---

## Project structure

```
app/src/main/java/com/dimmer/app/
├── data/
│   └── api/            Retrofit interface, API models
├── ui/
│   ├── home/           Home screen + ViewModel
│   ├── detail/         Detail screen + ViewModel
│   ├── search/         Search screen + ViewModel
│   ├── components/     Shared composables (FilmCard, ShimmerFilmCard)
│   └── theme/          Colors, typography, Material 3 theme
└── navigation/         NavGraph
```

---

## Acknowledgements

Film data provided by [TMDB](https://www.themoviedb.org/). This product uses the TMDB API but is not endorsed or certified by TMDB.

Typography: [Playfair Display](https://fonts.google.com/specimen/Playfair+Display) and [DM Sans](https://fonts.google.com/specimen/DM+Sans) via Google Fonts.
