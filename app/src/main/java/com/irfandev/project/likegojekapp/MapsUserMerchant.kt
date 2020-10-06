package com.irfandev.project.likegojekapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.internal.IGoogleMapDelegate
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.irfandev.project.likegojekapp.adapters.MapsListAdapter
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.helpers.Const
import com.irfandev.project.likegojekapp.helpers.PrefsHelper
import com.irfandev.project.likegojekapp.models.MapsModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_mapsmerchant.*
import java.lang.Exception


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsUserMerchant : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MapsListAdapter.LatLongfromAdapter {
    lateinit var map : GoogleMap
    private val TAG = MapsUserMerchant::class.java.simpleName
    lateinit var dbref : DatabaseReference
    lateinit var listmap : ArrayList<MapsModel>
    lateinit var mapadapter : MapsListAdapter
    lateinit var rcView : RecyclerView
    lateinit var tvError : TextView
    lateinit var loding : Dialog

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var lastLocation : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsmerchant)
        val maping = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        maping.getMapAsync(this)
        fabAddmap.setOnClickListener {
            showDialogAdd()
        }
        loding = AppsHelper.loadingDialog(this@MapsUserMerchant)
        loding.show()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsUserMerchant)
        listmap = ArrayList<MapsModel>()
        mapadapter = MapsListAdapter()
        rcView = findViewById(R.id.rcView)
        tvError = findViewById(R.id.tvError)
        rcView.layoutManager = LinearLayoutManager(this@MapsUserMerchant)
        rcView.setHasFixedSize(true)
        val uidx = PrefsHelper(this@MapsUserMerchant).getUID()
        dbref = FirebaseDatabase.getInstance().getReference("${Const.MERCHANTFBASE}/$uidx")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                AppsHelper.showShortToast(this@MapsUserMerchant, error.message)
                loding.dismiss()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    e("TAGCHECK", i.value.toString())
                    val modelmap = i.getValue(MapsModel::class.java)
                    listmap.add(modelmap!!)
                }
                rcView.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                mapadapter = MapsListAdapter(this@MapsUserMerchant, listmap, this@MapsUserMerchant)
                rcView.adapter = mapadapter
                loding.dismiss()
            }
        })

    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        val latitude = -6.2234748
        val longitude = 106.9476776
        val zoomLevel = 15.0f
        val homeLatLng = LatLng(latitude, longitude)
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
                location ->
            if(location != null){
                lastLocation = location
                val currentPost = LatLng(location.latitude, location.longitude)
                placeMarkerInMaps(currentPost)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPost
                    , 18.0f))
            }
        }
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

    fun showDialogAdd(){
        val dialogMap = Dialog(this@MapsUserMerchant)
        dialogMap.setContentView(R.layout.dialog_seletlocation)
        dialogMap.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)
        val settmap : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.frmap) as SupportMapFragment
        var gmap : GoogleMap? = null
        val etNamaLokasi : EditText = dialogMap.findViewById(R.id.etNamaLokasi)
        val etDetailLokasi : EditText = dialogMap.findViewById(R.id.etDetailLokasi)
        val btnSelct : AppCompatButton = dialogMap.findViewById(R.id.btnSelect)
        val btnClose :AppCompatButton = dialogMap.findViewById(R.id.btnClose)
        var latmap : Double = 0.0
        var longmap : Double = 0.0
        settmap.getMapAsync(object : OnMapReadyCallback{
            override fun onMapReady(p0: GoogleMap?) {
                gmap = p0
                val latitude = -6.2234748
                val longitude = 106.9476776
                val zoomLevel = 15.0f
                val homeLatLng = LatLng(latitude, longitude)
                gmap!!.uiSettings.isZoomControlsEnabled = true
                gmap!!.addMarker(MarkerOptions().position(homeLatLng).draggable(true))
                gmap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
                gmap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
                    override fun onMarkerDragEnd(p0: Marker?) {
                        latmap = p0!!.position.latitude
                        longmap = p0.position.longitude
                        return
                    }

                    override fun onMarkerDragStart(p0: Marker?) {
                        //do nothing
                    }

                    override fun onMarkerDrag(p0: Marker?) {
                        //do nothing
                    }

                })
                setMapStyle(gmap!!)
            }
        })
        btnSelct.setOnClickListener {
            if(etNamaLokasi.text.toString().isNotEmpty() && etDetailLokasi.text.toString().isNotEmpty()){
                val uid = PrefsHelper(this@MapsUserMerchant).getUID()
                val nilai = PrefsHelper(this@MapsUserMerchant).getCounter()
                dbref = FirebaseDatabase.getInstance().getReference("${Const.MERCHANTFBASE}/$uid/$nilai")
                dbref.child("mapname").setValue(etNamaLokasi.text.toString())
                dbref.child("mapdetail").setValue( etDetailLokasi.text.toString())
                dbref.child("maplatitude").setValue(latmap)
                dbref.child("maplongitude").setValue(longmap)
                PrefsHelper(this@MapsUserMerchant).setCounter(nilai + 1)
                AppsHelper.showShortToast(this@MapsUserMerchant, "data berhasil ditambahkan")
                startActivity(intent)
                dialogMap.dismiss()
                finish()
             }else{
                AppsHelper.showShortToast(this, "isilah setiap kolom kosong")
            }
        }
        btnClose.setOnClickListener {
            startActivity(intent)
            finish()
        }
        dialogMap.show()
    }

    fun placeMarkerInMaps(loc : LatLng){
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_market)
        ))
        map.addMarker(markerOptions)
    }

    override fun getLatLong(
        viewHolder: MapsListAdapter.MapsListViewHolder,
        mapPos: MapsModel,
        pos: Int
    ) {
        val loclat = listmap.get(viewHolder.adapterPosition).maplatitude
        val loclong = listmap.get(viewHolder.adapterPosition).maplongitude
        val currentPost = LatLng(loclat!!, loclong!!)
        placeMarkerInMaps(currentPost)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPost
            , 18.0f))
    }

}