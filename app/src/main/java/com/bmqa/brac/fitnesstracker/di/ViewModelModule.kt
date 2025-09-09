package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.presentation.viewmodel.FoodRecognitionViewModel
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    
    // ViewModels
    viewModel { FoodRecognitionViewModel(get()) }
    viewModel { GeminiFoodAnalysisViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}
