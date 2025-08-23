package com.bmqa.brac.fitnesstracker

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bmqa.brac.fitnesstracker.data.services.ClarifaiHelper

class ClarifaiTestActivity : AppCompatActivity() {
    
    private lateinit var textView: TextView
    private lateinit var testButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a simple layout programmatically
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        testButton = Button(this).apply {
            text = "Test Clarifai Food Recognition"
            setOnClickListener {
                testClarifaiAPI()
            }
        }
        
        textView = TextView(this).apply {
            text = "Results will appear here..."
            setPadding(0, 32, 0, 0)
        }
        
        layout.addView(testButton)
        layout.addView(textView)
        
        setContentView(layout)
    }
    
    private fun testClarifaiAPI() {
        textView.text = "Testing Clarifai API..."
        
        val clarifai = ClarifaiHelper()
        
        // Create a simple test image (1x1 pixel) for testing
        val testImageBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
        
        clarifai.predictImageBase64(testImageBase64) { result ->
            runOnUiThread {
                textView.text = result
            }
        }
    }
}
