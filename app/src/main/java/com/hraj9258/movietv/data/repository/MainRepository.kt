package com.hraj9258.movietv.data.repository

import com.hraj9258.movietv.data.model.Title
import com.hraj9258.movietv.data.model.TitleDetails
import com.hraj9258.movietv.data.remote.WatchmodeApiService
import io.reactivex.rxjava3.core.Single

class MainRepository(
    private val apiService: WatchmodeApiService
) {

    /**
     * Fetches movies and TV shows simultaneously and combines them into a Pair.
     * The first item of the Pair is the list of movies.
     * The second item is the list of TV shows.
     */
    fun getMoviesAndTvShows(): Single<Pair<List<Title>, List<Title>>> {
        // Create two Single sources: one for movies, one for TV shows.
        val moviesSingle = apiService.getTitles(types = "movie")
        val tvShowsSingle = apiService.getTitles(types = "tv_series")

        return Single.zip(
            moviesSingle.map { it.titles },
            tvShowsSingle.map { it.titles },
            // This function takes the result of each Single and combines them.
            // Here, we combine them into a Pair.
            { movies, tvShows -> Pair(movies, tvShows) }
        )
    }

    /**
     * Fetches details for a single title by its ID.
     */
    fun getTitleDetails(titleId: Int): Single<TitleDetails> {
        return apiService.getTitleDetails(titleId = titleId)
    }
}