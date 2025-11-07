package com.hraj9258.movietv.navigation.domain

import kotlinx.serialization.Serializable

sealed interface AppNavigationDestinations{
    @Serializable
    data object Home: AppNavigationDestinations

    @Serializable
    data class Details(val titleId: Int): AppNavigationDestinations
}