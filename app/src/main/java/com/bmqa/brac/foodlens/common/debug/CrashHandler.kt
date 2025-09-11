package com.bmqa.brac.foodlens.common.debug

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom crash handler to capture and log crashes
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var context: Context? = null
    
    companion object {
        private const val TAG = "CrashHandler"
        private const val CRASH_LOG_FILE = "crash_log.txt"
        
        @Volatile
        private var INSTANCE: CrashHandler? = null
        
        fun getInstance(): CrashHandler {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CrashHandler().also { INSTANCE = it }
            }
        }
    }
    
    fun init(context: Context) {
        this.context = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
    
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        logCrash(exception)
        defaultHandler?.uncaughtException(thread, exception)
    }
    
    private fun logCrash(exception: Throwable) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val crashInfo = buildString {
                appendLine("=== CRASH REPORT ===")
                appendLine("Timestamp: $timestamp")
                appendLine("Process ID: ${android.os.Process.myPid()}")
                appendLine("Thread: ${Thread.currentThread().name}")
                appendLine("Exception: ${exception.javaClass.simpleName}")
                appendLine("Message: ${exception.message}")
                appendLine("Stack Trace:")
                val stringWriter = StringWriter()
                exception.printStackTrace(PrintWriter(stringWriter))
                appendLine(stringWriter.toString())
                appendLine("=== END CRASH REPORT ===")
                appendLine()
            }
            
            // Log to system log
            Log.e(TAG, "App crashed: ${exception.message}", exception)
            
            // Save to file
            saveCrashToFile(crashInfo)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log crash", e)
        }
    }
    
    private fun saveCrashToFile(crashInfo: String) {
        try {
            val file = File(context?.filesDir, CRASH_LOG_FILE)
            FileWriter(file, true).use { writer ->
                writer.append(crashInfo)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save crash log to file", e)
        }
    }
    
    fun getCrashLogs(): String? {
        return try {
            val file = File(context?.filesDir, CRASH_LOG_FILE)
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read crash logs", e)
            null
        }
    }
    
    fun clearCrashLogs() {
        try {
            val file = File(context?.filesDir, CRASH_LOG_FILE)
            if (file.exists()) file.delete()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear crash logs", e)
        }
    }
}
