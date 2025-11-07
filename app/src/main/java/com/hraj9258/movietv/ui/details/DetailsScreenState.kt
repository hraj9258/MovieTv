package com.hraj9258.movietv.ui.details

import com.hraj9258.movietv.data.model.TitleDetails

data class DetailsScreenState(
    val isLoading: Boolean = false,
    val details: TitleDetails? = null,
    val error: String? = null
)