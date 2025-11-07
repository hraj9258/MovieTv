package com.hraj9258.movietv.ui.home

import com.hraj9258.movietv.data.model.Title

data class HomeScreenState(
    val isLoading: Boolean = false,
    val movies: List<Title> = emptyList(),
    val tvShows: List<Title> = emptyList(),
    val error: String? = null
)