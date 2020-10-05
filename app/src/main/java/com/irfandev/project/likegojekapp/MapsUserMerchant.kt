package com.irfandev.project.likegojekapp

import android.content.res.Resources
import android.os.Bundle
import android.util.Log.e
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import java.lang.Exception


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsUserMerchant : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    lateinit var map : GoogleMap
    private val TAG = MapsUserMerchant::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsmerchant)
        val maping = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        maping.getMapAsync(this)
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

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    fun setMapStyle(p0 : GoogleMap){
        try{
            val success = p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsUserMerchant, R.raw.map_style))
            if(!success){
                e(TAG, "error locking for map style")
            }
        }catch (err : Resources.NotFoundException){
            e(TAG, "can't find style : ", err)
        }
    }
}