package com.rookie.code.presentation.navigation

import com.rookie.code.domain.entities.GeminiFoodAnalysis
import java.util.concurrent.ConcurrentHashMap

/**
 * Manages navigation data to avoid passing large objects through navigation routes
 */
object NavigationDataManager {
    private val dataStore = ConcurrentHashMap<String, GeminiFoodAnalysis>()
    
    fun storeAnalysis(analysis: GeminiFoodAnalysis): String {
        val id = "analysis_${System.currentTimeMillis()}_${analysis.hashCode()}"
        dataStore[id] = analysis
        return id
    }
    
    fun getAnalysis(id: String): GeminiFoodAnalysis? {
        return dataStore[id]
    }
    
    fun removeAnalysis(id: String) {
        dataStore.remove(id)
    }
    
    fun clearAll() {
        dataStore.clear()
    }
}
