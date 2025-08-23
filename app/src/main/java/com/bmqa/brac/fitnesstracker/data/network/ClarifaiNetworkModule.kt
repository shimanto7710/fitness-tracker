package com.bmqa.brac.fitnesstracker.data.network

import android.content.Context
import com.bmqa.brac.fitnesstracker.data.services.ClarifaiService

object ClarifaiNetworkModule {
    
    fun createClarifaiService(context: Context): ClarifaiService {
        return ClarifaiService(context)
    }
}
