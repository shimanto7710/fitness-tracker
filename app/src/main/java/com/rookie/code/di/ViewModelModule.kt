package com.rookie.code.di

import com.rookie.code.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisViewModel
import com.rookie.code.presentation.features.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    
    // ViewModels
    viewModel { GeminiFoodAnalysisViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}
