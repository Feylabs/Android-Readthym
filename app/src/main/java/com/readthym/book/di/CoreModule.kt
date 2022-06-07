package com.readthym.book.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.henrylabs.qumparan.data.remote.service.ApiService
import com.readthym.book.data.AppRepository
import com.readthym.book.data.remote.RemoteDataSource
import com.readthym.book.utils.Network
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )

            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request =
                        chain.request().newBuilder().addHeader("token", "PUT_YOUR_API_KEY")
                            .build()
                    return chain.proceed(request)
                }
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()

        retrofit.create(ApiService::class.java)
    }


}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { AppRepository(get()) }
}
