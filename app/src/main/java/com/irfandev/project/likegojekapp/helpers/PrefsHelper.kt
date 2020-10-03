package com.irfandev.project.likegojekapp.helpers

import android.content.Context


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class PrefsHelper(ctx : Context) {
    private val APPSNAME = "LIKEGOJEKAPPS"

    private val prefshelp = ctx.getSharedPreferences(APPSNAME, Context.MODE_PRIVATE)
    private val editMode = prefshelp.edit()

}