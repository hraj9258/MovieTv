package com.hraj9258.movietv.di

import com.google.gson.GsonBuilder
import com.hraj9258.movietv.data.remote.WatchmodeApiService
import com.hraj9258.movietv.data.repository.MainRepository
import com.hraj9258.movietv.ui.details.DetailsViewModel
import com.hraj9258.movietv.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.watchmode.com/v1/"
val networkModule = module {
    // Provide OkHttpClient
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(WatchmodeApiService::class.java)
    }
}

val repositoryModule = module {
    single { MainRepository(get()) }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModel{ (titleId: Int) -> DetailsViewModel(titleId, get()) }
}