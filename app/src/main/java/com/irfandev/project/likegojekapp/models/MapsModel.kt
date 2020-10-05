package com.irfandev.project.likegojekapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsModel : Serializable{
    @SerializedName("mapname")
    var mapname : String ?= null

    @SerializedName("mapdetail")
    var mapdetail : String ?= null

    @SerializedName("maplatitude")
    var maplatitude : String ?= null

    @SerializedName("maplongitude")
    var maplongitude : String ?= null
}