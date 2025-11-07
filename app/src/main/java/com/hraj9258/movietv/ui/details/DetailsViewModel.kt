package com.hraj9258.movietv.ui.details

import androidx.lifecycle.ViewModel
import com.hraj9258.movietv.data.repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailsViewModel(
    private val titleId: Int,
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsScreenState())
    val uiState = _uiState.asStateFlow()

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchTitleDetails()
    }

    private fun fetchTitleDetails() {
        _uiState.update { it.copy(isLoading = true) }

        val disposable = repository.getTitleDetails(titleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // onSuccess
                { details ->
                    _uiState.update {
                        it.copy(isLoading = false, details = details)
                    }
                },
                // onError
                { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "An error occurred")
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