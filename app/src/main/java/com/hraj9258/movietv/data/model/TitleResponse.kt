package com.hraj9258.movietv.data.model

import com.google.gson.annotations.SerializedName

data class TitleResponse(
    @SerializedName("titles")
    val titles: List<Title>
)