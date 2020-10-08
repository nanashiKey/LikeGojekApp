package com.irfandev.project.likegojekapp.activities

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.directions.route.*
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
class MapsUserBiasa : AppCompatActivity() ,
    OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener, RoutingListener{
    lateinit var map : GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var lastLocation : Location
    lateinit var dbref : DatabaseReference
    lateinit var listmap : ArrayList<MapsModel>
    lateinit var dialog: Dialog
    var currentPoly : Polyline ?= null
    var polylines : ArrayList<Polyline> ?= null
    var start : LatLng? = null
    var end : LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsbiasa)
        val mapping = supportFragmentManager.findFragmentById(R.id.frmap) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsUserBiasa)
        mapping.getMapAsync(this)
        listmap = ArrayList<MapsModel>()
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
                            listmap.add(modelmap!!)
                            val currentPost = LatLng(modelmap.maplatitude!!, modelmap.maplongitude!!)
                            placeMarkerInMaps(currentPost, R.mipmap.ic_market, modelmap.mapname!!)
                        }
                    }
                }
            }

        })
        dialog = Dialog(this@MapsUserBiasa)
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
                start = LatLng(location.latitude, location.longitude)
                placeMarkerInMaps(start!!, R.mipmap.ic_ppl, "Lokasi Anda")
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(start
                    , 18.0f))
            }
            map.setOnMarkerClickListener(this)
        }
    }

    override fun onMapClick(p0: LatLng?) {

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

    fun placeMarkerInMaps(loc : LatLng, mipmap : Int, titlex : String){
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources,
                    mipmap
                )
            )).title(titlex)
        map.addMarker(markerOptions)
    }

    fun showgotoDialog(dialog: Dialog, latLngTujuan: LatLng){
        dialog.setContentView(R.layout.dialog_gotolocation)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btnOke)
        val btnBatal = dialog.findViewById<AppCompatButton>(R.id.btnBatal)
        btnOke.setOnClickListener {
            findRoutes(start!!, latLngTujuan)
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        end = LatLng(p0!!.position.latitude, p0.position.longitude)
        showgotoDialog(dialog, end!!)
        return false
    }

    override fun onRoutingCancelled() {
        findRoutes(start!!, end!!)
    }

    override fun onRoutingStart() {
    }

    override fun onRoutingFailure(p0: RouteException?) {
        p0!!.printStackTrace()
    }

    override fun onRoutingSuccess(p0: java.util.ArrayList<Route>?, p1: Int) {
        if (polylines != null) {
            polylines!!.clear()
        }
        val polyOptions = PolylineOptions()

        polylines = ArrayList<Polyline>()
        for (i in 0 until p0!!.size) {
            if (i == p1) {
                polyOptions.color(resources.getColor(R.color.colorPrimaryDark))
                polyOptions.width(5f)
                polyOptions.addAll(p0.get(p1).getPoints())
                if(currentPoly != null){
                    currentPoly!!.remove()
                }
                currentPoly = map.addPolyline(polyOptions)
                polylines!!.add(currentPoly!!)
            }
        }

    }

    fun findRoutes(start : LatLng?, end : LatLng?){
        if (start == null || end == null) {
            AppsHelper.showShortToast(this@MapsUserBiasa, "Gagal Mengammbil lokasi")
        } else {
            val routing: Routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .key(resources.getString(R.string.maps_api_key))
                .build()
            routing.execute()
        }
    }

}