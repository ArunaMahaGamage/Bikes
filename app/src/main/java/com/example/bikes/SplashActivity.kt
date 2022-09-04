package com.example.bikes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ startApp() },2000)
    }

    private fun startApp() {
        val activityIntent = Intent(this, MainActivity::class.java)
        activityIntent.action = intent?.action
        activityIntent.data = intent?.data
        startActivity(activityIntent)
        finish()
    }
}