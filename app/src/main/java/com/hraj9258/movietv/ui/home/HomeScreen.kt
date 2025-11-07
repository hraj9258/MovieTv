package com.hraj9258.movietv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hraj9258.movietv.data.model.Title
import com.hraj9258.movietv.ui.components.ShimmerListItem
import com.hraj9258.movietv.ui.theme.MovieTVTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    onTitleClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onTitleClick = onTitleClick,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenState,
    modifier: Modifier = Modifier,
    onTitleClick: (Int) -> Unit = {}
) {

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text("Discover",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
            )
        },
        modifier = modifier
    ) { paddingValues ->
        // Error Handling
        LaunchedEffect(uiState.error) {
            uiState.error?.let {
                snackBarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long
                )
            }
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (uiState.isLoading) {
                LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(10) {
                        ShimmerListItem()
                    }
                }
            } else {
                val itemsToShow = when (selectedTabIndex) {
                    0 -> uiState.movies
                    1 -> uiState.tvShows
                    else -> emptyList()
                }

                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(itemsToShow) { title ->
                        TitleListItem(title = title, onItemClick = onTitleClick)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    MovieTVTheme {
        HomeScreen(
            uiState = HomeScreenState(
                isLoading = false,
                movies = listOf(
                    Title(
                        id = 1,
                        type = "movie",
                        title = "Movie 1",
                        year = -1,
                        imdbId = "IMDB ID",
                        tmdbId = -1,
                        tmdbType = "TMDB Type",
                        poster = "Banner Link"
                    ),
                    Title(
                        id = 2,
                        type = "movie",
                        title = "Movie 2",
                        year = -2,
                        imdbId = "IMDB ID",
                        tmdbId = -2,
                        tmdbType = "TMDB Type",
                        poster = "Banner Link"
                    ),
                )
            )
        )
    }
}

@Preview
@Composable
private fun HomeScreenLoadingPreview() {
    MovieTVTheme {
        HomeScreen(
            uiState = HomeScreenState(
                isLoading = true
            )
        )
    }
}