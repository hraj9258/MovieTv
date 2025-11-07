package com.hraj9258.movietv.ui.home

import androidx.lifecycle.ViewModel
import com.hraj9258.movietv.data.repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    // CompositeDisposable to hold our RxJava subscriptions.
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchMoviesAndTvShows()
    }

    fun fetchMoviesAndTvShows() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val disposable = repository.getMoviesAndTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // onSuccess
                 { (movies, tvShows) ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            movies = movies,
                            tvShows = tvShows
                        )
                    }
                },
                // onError
                { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}