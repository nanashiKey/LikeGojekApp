package com.irfandev.project.likegojekapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.irfandev.project.likegojekapp.R
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.helpers.Const
import com.irfandev.project.likegojekapp.models.MapsModel


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsUserBiasa : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMapClickListener{
    lateinit var map : GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var lastLocation : Location
    lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsbiasa)
        val mapping = supportFragmentManager.findFragmentById(R.id.frmap) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsUserBiasa)
        mapping.getMapAsync(this)
        dbref = FirebaseDatabase.getInstance().getReference(Const.MERCHANTFBASE)
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                e("TAGERROR", error.message)
                AppsHelper.showShortToast(this@MapsUserBiasa, error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    if(i.value != null){
                        for(data in i.children){
                            e("TAGCHECK", data.value.toString())
                            val modelmap = data.getValue(MapsModel::class.java)
                            val currentPost = LatLng(modelmap!!.maplatitude!!, modelmap.maplongitude!!)
                            placeMarkerInMaps(currentPost, R.mipmap.ic_market)
                        }
                    }
                }
            }

        })
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        map.uiSettings.isZoomControlsEnabled = true
        setMapStyle(map)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
                location ->
            if(location != null){
                lastLocation = location
                val currentPost = LatLng(location.latitude, location.longitude)

                placeMarkerInMaps(currentPost, R.mipmap.ic_ppl)
                map.addCircle(
                    CircleOptions()
                        .center(currentPost)
                        .radius(10000.0)
                        .strokeColor(Color.RED)
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPost
                    , 18.0f))
            }
        }
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

    fun placeMarkerInMaps(loc : LatLng, mipmap : Int){
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources,
                    mipmap
                )
            ))
        map.addMarker(markerOptions)
    }
}