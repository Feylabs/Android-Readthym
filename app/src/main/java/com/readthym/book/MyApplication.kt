package com.readthym.book

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.readthym.book.di.networkModule
import com.readthym.book.di.repositoryModule
import com.readthym.book.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MyApplication :  Application()  {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(getApplicationContext());

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(listOf(
                networkModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }

}