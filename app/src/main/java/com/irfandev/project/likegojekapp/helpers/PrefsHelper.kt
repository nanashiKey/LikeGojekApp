package com.irfandev.project.likegojekapp.helpers

import android.content.Context
import com.irfandev.project.likegojekapp.models.UserModels


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class PrefsHelper(ctx : Context) {
    private val APPSNAME = "LIKEGOJEKAPPS"
    private val UID = "UIDUSERS"
    private val USERNAME = "USERNAME"
    private val USERTYPE = "USERTYPE"
    private val USEREMAIL = "USERMAIL"
    private val USERLOGIN = "USERLOGIN"

    private val prefshelp = ctx.getSharedPreferences(APPSNAME, Context.MODE_PRIVATE)
    private val editMode = prefshelp.edit()


    fun setUserMode(user : UserModels){
        editMode.putString(UID, user.uid)
        editMode.putString(USEREMAIL, user.email)
        editMode.putString(USERNAME, user.username)
        editMode.putString(USERTYPE, user.userType)
        editMode.apply()
    }

    fun getUname() : String?{
        return  prefshelp.getString(USERNAME, "")
    }

    fun getEmail() : String?{
        return prefshelp.getString(USEREMAIL, "")
    }

    fun getUID() : String?{
        return  prefshelp.getString(UID, "")
    }

    fun getUType() : String?{
        return  prefshelp.getString(USERTYPE, "")
    }

    fun setStatusLogin(status : Boolean){
        editMode.putBoolean(USERLOGIN, status)
        editMode.apply()
    }

    fun getStatusLogin() : Boolean{
        return prefshelp.getBoolean(USERLOGIN, false)
    }


}