package com.example.cardviewdemo.GoogleMaps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.cardviewdemo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
     var fusedLocationProviderClient: FusedLocationProviderClient? = null
     var currentLocation: Location? = null
    var currentMarker:Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

  //      binding = ActivityMapsBinding.inflate(layoutInflater)
    //    setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Google Maps"
        actionBar.setDisplayHomeAsUpEnabled(true)

        //currentLocation = Location("providerNA")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btFetchLocation1.setOnClickListener {
            fetchLocation()

        }

    }

    private fun fetchLocation() {
        if(ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }

        val task = fusedLocationProviderClient?.lastLocation
            task?.addOnSuccessListener { location ->
                if (location != null){
                    this.currentLocation = location
                    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1000 ->    if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            fetchLocation()
                        }else     {Toast.makeText(this,"Permissions Denied", Toast.LENGTH_LONG).show()
                                    startActivityForResult( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0)
                        }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

       /* val sydney = LatLng(-34.0,151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Make in sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        drawMarker(latLng)

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {
                if (currentMarker != null)
                    currentMarker?.remove()

                val newLatLng = LatLng(p0.position.latitude,p0.position.latitude)
                drawMarker(newLatLng)

            }

            override fun onMarkerDragEnd(p0: Marker) {
            }

            override fun onMarkerDragStart(p0: Marker) {
            }


        })
    }

    fun drawMarker(latLng: LatLng) {
       val markerOption = MarkerOptions().position(latLng).title("I am here!")
            .snippet(getAddress(latLng.latitude,latLng.longitude)).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        currentMarker = mMap.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }

     fun getAddress(latitude: Double, longitude: Double):String?{
         val geocoder = Geocoder(this, Locale.getDefault())
         val addresses = geocoder.getFromLocation(latitude,longitude,1)
         return  addresses[0].getAddressLine(0).toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}