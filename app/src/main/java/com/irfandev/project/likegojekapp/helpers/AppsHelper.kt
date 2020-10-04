package com.irfandev.project.likegojekapp.helpers

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
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
    }
}