package com.example.bikes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    lateinit var iv_background: ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        Handler().postDelayed({ startApp() },2000)

        iv_background = findViewById(R.id.imageView2);
        iv_background.setOnClickListener {
            startApp()
        }
    }

    private fun startApp() {
        val activityIntent = Intent(this, MainActivity::class.java)
        activityIntent.action = intent?.action
        activityIntent.data = intent?.data
        startActivity(activityIntent)
        finish()
    }
}