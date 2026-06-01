package com.dimmer.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.dimmer.app.data.api.Movie
import com.dimmer.app.data.api.MovieDetail
import com.dimmer.app.ui.theme.NavyBlack
import com.dimmer.app.ui.theme.RatingGold
import com.dimmer.app.ui.theme.SteelBlue

private const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500"

@Composable
fun rememberPaletteColor(imageUrl: String?): Color {
    val context = LocalContext.current
    var color by remember(imageUrl) { mutableStateOf(SteelBlue) }

    LaunchedEffect(imageUrl) {
        if (imageUrl == null) return@LaunchedEffect
        try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = context.imageLoader.execute(request)
            if (result is SuccessResult) {
                val bitmap = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
                bitmap?.let {
                    Palette.from(it).generate { palette ->
                        val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
                        swatch?.let { s ->
                            color = Color(s.rgb)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            color = SteelBlue
        }
    }

    return color
}

@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    onMovieClick: (Int) -> Unit = {},
    viewModel: DetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val similarMovies by viewModel.similarMovies.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBlack)
    ) {
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(
                    color = SteelBlue,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Couldn't load film",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = { viewModel.fetchMovieDetails(movieId) }) {
                        Text("Retry", color = SteelBlue)
                    }
                }
            }

            is DetailUiState.Success -> {
                DetailContent(
                    movie = state.movie,
                    similarMovies = similarMovies,
                    onMovieClick = onMovieClick
                )
            }
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

@Composable
fun DetailContent(
    movie: MovieDetail,
    similarMovies: List<Movie>,
    onMovieClick: (Int) -> Unit
) {
    val posterUrl = movie.posterPath?.let { TMDB_IMAGE_BASE + it }
    val glowColor = rememberPaletteColor(posterUrl)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.4f),
                                NavyBlack
                            )
                        )
                    )
            )

            AsyncImage(
                model = posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                NavyBlack.copy(alpha = 0.6f),
                                NavyBlack
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            val year = movie.releaseDate?.take(4) ?: ""
            val runtime = movie.runtime?.let { "${it}m" } ?: ""
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (year.isNotEmpty()) {
                    Text(
                        text = year,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (runtime.isNotEmpty()) {
                    Text(
                        text = runtime,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "★ ${"%.1f".format(movie.voteAverage)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = RatingGold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (movie.genres.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(movie.genres) { genre ->
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = SteelBlue,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = SteelBlue.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            val providers = movie.watchProviders?.flatrate
            if (!providers.isNullOrEmpty()) {
                Text(
                    text = "WHERE TO WATCH",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(providers) { provider ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                model = provider.logoPath?.let { TMDB_IMAGE_BASE + it },
                                contentDescription = provider.providerName,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Text(
                                text = provider.providerName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!movie.overview.isNullOrEmpty()) {
                Text(
                    text = "SYNOPSIS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (similarMovies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "MORE LIKE THIS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(similarMovies) { movie ->
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .clickable { onMovieClick(movie.id) }
                        ) {
                            AsyncImage(
                                model = movie.posterPath?.let { TMDB_IMAGE_BASE + it },
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2f / 3f)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}