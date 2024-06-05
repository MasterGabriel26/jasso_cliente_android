package com.example.moveeventossaltillo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.moveeventossaltillo.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnContinuar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
//        Handler(Looper.myLooper()!!).postDelayed(
//            {
//                loop(MainActivity::class.java)
//            }, 4000
//        )
    }
    fun loop(activityOpen: Class<*>) {
        val intent = Intent(this, activityOpen)
        startActivity(intent)
        finish()
    }
}