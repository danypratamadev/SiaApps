package com.example.sia_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sia_app.admin.HomeAdminActivity
import com.example.sia_app.dosen.HomeDosenActivity
import com.example.sia_app.mhs.HomeMhsActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseApp.initializeApp(this);
        auth = Firebase.auth
        sharedPref = this.getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)

        val currentUser = auth.currentUser

        Timer().schedule(object : TimerTask() {
            override fun run() {
                if(currentUser != null){
                    when (sharedPref.getInt("roleUser", 100)) {
                        10 -> {
                            startActivity(Intent(this@SplashActivity, HomeAdminActivity::class.java))
                        }
                        20 -> {
                            startActivity(Intent(this@SplashActivity, HomeDosenActivity::class.java))
                        }
                        30 -> {
                            startActivity(Intent(this@SplashActivity, HomeMhsActivity::class.java))
                        }
                        else -> {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        }
                    }
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }, 2000)

    }
}