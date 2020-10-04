package com.irfandev.project.likegojekapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.helpers.PrefsHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWelcome.text = "Selamat Datang ${PrefsHelper(this@MainActivity).getUname()}"

        imgExit.setOnClickListener {
            AppsHelper.goTo(this@MainActivity, LoginActivity::class.java)
            PrefsHelper(this@MainActivity).setStatusLogin(false)
            finish()
        }
    }
}