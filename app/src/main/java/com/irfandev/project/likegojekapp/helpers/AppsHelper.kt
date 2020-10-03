package com.irfandev.project.likegojekapp.helpers

import android.content.Context
import android.content.Intent
import android.widget.Toast


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
    }
}