package com.irfandev.project.likegojekapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import kotlinx.android.synthetic.main.activity_login.*


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            AppsHelper.goTo(this@LoginActivity, MainActivity::class.java)
            finish()
        }
        tvRegister.setOnClickListener {
            AppsHelper.goTo(this@LoginActivity, RegisterActivity::class.java)
        }
    }
}