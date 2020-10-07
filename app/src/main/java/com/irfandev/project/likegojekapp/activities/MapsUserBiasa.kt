package com.irfandev.project.likegojekapp.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.irfandev.project.likegojekapp.R


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsUserBiasa : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMapClickListener{
    lateinit var map : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsbiasa)
        val mapping = supportFragmentManager.findFragmentById(R.id.frmap) as SupportMapFragment
        mapping.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        val latitude = -6.2234748
        val longitude = 106.9476776
        val zoomLevel = 15.0f
        val homeLatLng = LatLng(latitude, longitude)
        map.uiSettings.isZoomControlsEnabled = true
        map.addMarker(MarkerOptions().position(homeLatLng).title("It's Home"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        setMapStyle(map)
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }

    fun setMapStyle(p0 : GoogleMap){
        try{
            val success = p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsUserBiasa,
                R.raw.map_style
            ))
            if(!success){
                Log.e("TAGERROR", "error locking for map style")
            }
        }catch (err : Resources.NotFoundException){
            Log.e("TAGERROR", "can't find style : ", err)
        }
    }
}