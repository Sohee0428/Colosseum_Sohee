package com.example.colosseum_sohee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
    }

    override fun setValues() {
    }
}