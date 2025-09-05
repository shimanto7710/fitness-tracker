package com.bmqa.brac.fitnesstracker

import android.app.Application
import com.bmqa.brac.fitnesstracker.di.networkModule
import com.bmqa.brac.fitnesstracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitnessTrackerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@FitnessTrackerApplication)
            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}
