package com.bmqa.brac.foodlens

import android.app.Application
import com.bmqa.brac.foodlens.common.debug.CrashHandler
import com.bmqa.brac.foodlens.di.networkModule
import com.bmqa.brac.foodlens.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FoodLensApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize crash handler
        CrashHandler.getInstance().init(this)
        
        startKoin {
            androidContext(this@FoodLensApplication)
            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}
