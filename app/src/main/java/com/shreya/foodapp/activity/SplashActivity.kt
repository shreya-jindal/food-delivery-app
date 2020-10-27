package com.shreya.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.shreya.foodapp.R

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences=getSharedPreferences("FoodappPreference", Context.MODE_PRIVATE)

        Handler().postDelayed({

            val intent= Intent(this@SplashActivity,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        },1000)

    }
}
