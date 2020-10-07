package com.irfandev.project.likegojekapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.irfandev.project.likegojekapp.R
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import kotlinx.android.synthetic.main.activity_register.*


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class RegisterActivity : AppCompatActivity() {
    lateinit var fauth : FirebaseAuth
    lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        fauth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            val passCheck = etPasswordCheck.text.toString()
            doRegister(username, email, pass, passCheck)
        }
    }

    fun doRegister(uname : String, mail : String, pass : String, pass2 : String){
        val userType = spinUserType.selectedItem
        val loading = AppsHelper.loadingDialog(this@RegisterActivity)
        if(uname.isNotEmpty() && mail.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()){
            loading.show()
            if(pass.equals(pass2)){
                fauth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val uidUser = it.result!!.user!!.uid
                        dbref = FirebaseDatabase.getInstance().getReference("pengguna/$uidUser")
                        dbref.child("uid").setValue(uidUser)
                        dbref.child("username").setValue(uname)
                        dbref.child("email").setValue(mail)
                        dbref.child("usertype").setValue(userType)
                        AppsHelper.showLongToast(this@RegisterActivity, "Registrasi Berhasil")
                        loading.dismiss()
                        onBackPressed()
                    }else{
                        loading.dismiss()
                        AppsHelper.showShortToast(this@RegisterActivity, "Registrasi Gagal")
                    }
                }.addOnFailureListener {
                    AppsHelper.showShortToast(this@RegisterActivity, "Registrasi Gagal ${it.message}")
                    it.printStackTrace()
                    loading.dismiss()
                }
            }else{
                AppsHelper.showShortToast(this@RegisterActivity, "Registrasi Gagal")
                etPassword.error = "Password tidak sama"
                etPasswordCheck.error = "Password tidak sama"
            }
        }else{
            AppsHelper.showShortToast(this@RegisterActivity, "isilah setiap kolom yang masih kosong")
        }
    }
}