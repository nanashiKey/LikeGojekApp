package com.irfandev.project.likegojekapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class UserModels : Serializable {
    @SerializedName("uid")
    var uid : String ?= null

    @SerializedName("email")
    var email : String ?= null

    @SerializedName("username")
    var username : String ?= null

    @SerializedName("usertype")
    var usertype : String ?= null
}