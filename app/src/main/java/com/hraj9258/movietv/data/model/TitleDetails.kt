package com.hraj9258.movietv.data.model

import com.google.gson.annotations.SerializedName

data class TitleDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("year")
    val year: Int,

    @SerializedName("plot_overview")
    val plotOverview: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("poster")
    val poster: String?
)