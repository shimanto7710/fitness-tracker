package com.bmqa.brac.foodlens.di

import com.bmqa.brac.foodlens.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.foodlens.presentation.features.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    
    // ViewModels
    viewModel { GeminiFoodAnalysisViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}
