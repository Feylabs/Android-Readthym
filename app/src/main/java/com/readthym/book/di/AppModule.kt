package com.readthym.book.di

import com.readthym.book.ui.SharedViewModel
import com.readthym.book.ui.auth.AuthViewModel
import com.readthym.book.ui.book.DetailBookViewModel
import com.readthym.book.ui.rythm_home.RythmHomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { SharedViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { RythmHomeViewModel(get()) }
    viewModel { DetailBookViewModel(get()) }
}

