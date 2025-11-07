package com.hraj9258.movietv.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hraj9258.movietv.data.model.TitleDetails
import com.hraj9258.movietv.ui.components.shimmerBrush
import com.hraj9258.movietv.ui.theme.MovieTVTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenRoot(
    onNavigateBack: () -> Unit,
    titleId: Int?
) {
    if (titleId == null) {
        EmptySelection()
    } else {
        val viewModel: DetailsViewModel = koinViewModel(parameters = { parametersOf(titleId) })
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        DetailsScreen(
            uiState = uiState,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
fun EmptySelection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Please Select an Item to get its details")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiState: DetailsScreenState,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.details?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        // Error handling
        LaunchedEffect(uiState.error) {
            uiState.error?.let {
                snackBarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
            }
        }
        if (uiState.isLoading) {
            DetailsShimmer(paddingValues)
        } else {
            uiState.details?.let { details ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(details.poster)
                            .crossfade(true)
                            .build(),
                        contentDescription = details.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = details.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Release Date: ${details.releaseDate}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = details.plotOverview ?: "No description available.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsShimmer(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(shimmerBrush())
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(30.dp)
                .background(shimmerBrush())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .background(shimmerBrush())
        )
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    MovieTVTheme {
        DetailsScreen(
            uiState = DetailsScreenState(
                isLoading = false,
                details = TitleDetails(
                    id = -1,
                    year = -1,
                    title = "Title",
                    releaseDate = "2023-01-01",
                    plotOverview = "Plot Overview",
                    poster = "Poster URL",
                )
            ),
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun DetailsScreenLoadingPreview() {
    MovieTVTheme {
        DetailsScreen(
            uiState = DetailsScreenState(
                isLoading = true
            ),
            onNavigateBack = {}
        )
    }
}