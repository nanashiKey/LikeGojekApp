package com.irfandev.project.likegojekapp

import android.os.Bundle
import android.util.Log.e
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.helpers.PrefsHelper
import com.irfandev.project.likegojekapp.models.UserModels
import kotlinx.android.synthetic.main.activity_login.*


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class LoginActivity : AppCompatActivity() {

    lateinit var dbref : DatabaseReference
    lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference("pengguna")
        val statusLogin = PrefsHelper(this@LoginActivity).getStatusLogin()
        if(statusLogin){
            AppsHelper.goTo(this@LoginActivity, MainActivity::class.java)
            finish()
        }



        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            doLogin(email, password)
        }
        tvRegister.setOnClickListener {
            AppsHelper.goTo(this@LoginActivity, RegisterActivity::class.java)
        }
    }

    fun doLogin(email : String, pass : String){
        if(email.isNotEmpty() && pass.isNotEmpty()){
            val loading = AppsHelper.loadingDialog(this@LoginActivity)
            loading.show()
            fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if(it.isSuccessful){
                    dbref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            e("TAGERROR", error.message)
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children){
                                val users = data.getValue(UserModels::class.java)
                                if(email.equals(users!!.email)){
                                    loading.dismiss()
                                    AppsHelper.showShortToast(this@LoginActivity, "Login Berhasil")
                                    PrefsHelper(this@LoginActivity).setUserMode(users)
                                    PrefsHelper(this@LoginActivity).setStatusLogin(true)
                                    AppsHelper.goTo(this@LoginActivity, MainActivity::class.java)
                                    finish()
                                }
                            }
                        }

                    })
                }else{
                    loading.dismiss()
                    AppsHelper.showShortToast(this@LoginActivity, "Login gagal ${it.exception!!.message}")
                }
            }.addOnFailureListener {
                it.printStackTrace()
                loading.dismiss()
                AppsHelper.showShortToast(this@LoginActivity, "Login gagal, ${it.message}")
            }
        }else{
            AppsHelper.showShortToast(this@LoginActivity, "Silahkan isi setiap kolom kosong")
        }
    }
}