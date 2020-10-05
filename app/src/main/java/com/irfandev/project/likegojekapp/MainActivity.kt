package com.irfandev.project.likegojekapp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.helpers.Const
import com.irfandev.project.likegojekapp.helpers.PrefsHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWelcome.text = "Selamat Datang ${PrefsHelper(this@MainActivity).getUname()}"
        val usertype = PrefsHelper(this@MainActivity).getUType()
        AppsHelper.showShortToast(this@MainActivity, "usertype : $usertype")
        if(usertype!!.toLowerCase(Locale("in", "ID")).equals("merchant")){
            btnCariMarket.visibility = View.GONE
            btnLihatMarket.visibility = View.VISIBLE
        }else{
            btnCariMarket.visibility = View.VISIBLE
            btnLihatMarket.visibility = View.GONE
        }
        imgExit.setOnClickListener {
            AppsHelper.goTo(this@MainActivity, LoginActivity::class.java)
            PrefsHelper(this@MainActivity).setStatusLogin(false)
            finish()
        }
        btnCariMarket.setOnClickListener {
            AppsHelper.goTo(this@MainActivity, MapsUserBiasa::class.java)
        }

        btnLihatMarket.setOnClickListener {
            AppsHelper.goTo(this@MainActivity, MapsUserMerchant::class.java)
        }

        val cekPermission = AppsHelper.cekMapsPermission(this@MainActivity)
        if(!cekPermission){
            AppsHelper.doMapsPermission(this@MainActivity)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            Const.REQPERMISSION ->{
                if((grantResults!=null && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    AppsHelper.showShortToast(this@MainActivity, "Permission Granted")
                }else{
                    AppsHelper.showShortToast(this@MainActivity, "Permission denied")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}