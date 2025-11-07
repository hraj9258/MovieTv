package com.hraj9258.movietv.data.remote

import com.hraj9258.movietv.BuildConfig
import com.hraj9258.movietv.data.model.TitleDetails
import com.hraj9258.movietv.data.model.TitleResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchmodeApiService {
    @GET("list-titles")
    fun getTitles(
        @Query("apiKey") apiKey: String = BuildConfig.WATCHMODE_API_KEY,
        @Query("types") types: String, // e.g., "movie", "tv_series"
        @Query("limit") limit: Int = 20
    ): Single<TitleResponse>

    @GET("title/{title_id}/details")
    fun getTitleDetails(
        @Path("title_id") titleId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.WATCHMODE_API_KEY,
    ): Single<TitleDetails>
}