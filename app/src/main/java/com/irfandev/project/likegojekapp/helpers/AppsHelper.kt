package com.irfandev.project.likegojekapp.helpers

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.irfandev.project.likegojekapp.R


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class AppsHelper {
    companion object{
        fun showShortToast(ctx : Context, msg : String){
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(ctx : Context, msg : String){
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
        }

        fun <T : Any> goTo(ctx: Context , to : Class<T>){
            ctx.startActivity(Intent(ctx, to))
        }

        fun loadingDialog(ctx: Context) : Dialog{
            val dialog = Dialog(ctx)
            dialog.setContentView(R.layout.loading_screen)
            dialog.setCancelable(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            return dialog
        }

        fun cekMapsPermission(ctx : Context) : Boolean{
            return PermissionChecker.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                    && PermissionChecker.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
        }

        fun doMapsPermission(saved : AppCompatActivity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permisi = arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (PermissionChecker.checkSelfPermission(saved, Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED
                    && PermissionChecker.checkSelfPermission( saved, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PermissionChecker.PERMISSION_GRANTED
                ) {
                    saved.requestPermissions(permisi, Const.REQPERMISSION)
                }
            }
        }
    }
}