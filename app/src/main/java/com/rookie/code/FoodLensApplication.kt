package com.rookie.code

import android.app.Application
import com.rookie.code.common.debug.CrashHandler
import com.rookie.code.di.networkModule
import com.rookie.code.di.viewModelModule
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
