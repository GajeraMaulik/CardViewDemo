package com.example.cardviewdemo.GoogleMaps

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cardviewdemo.R
import com.example.cardviewdemo.constants.Constants
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_google_maps.*
import kotlinx.android.synthetic.main.activity_movie.*
import java.util.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback  {

     var fusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var resultReceiver: ResultReceiver
    private  var currentLocation: Location? = null
    private var REQUEST_LOCATION_CODE = 1000
    lateinit var latLng:LatLng
     var mMap:GoogleMap? = null
    var currentMarker:Marker? = null


    private val permissionCode = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        val actionBar = supportActionBar
        actionBar!!.title = "Google Maps"
        actionBar.setDisplayHomeAsUpEnabled(true)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

     //  resultReceiver = AddressResultReceiver(Handler())




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setUpMap()

        btShowLocation.setOnClickListener{
                getCurrentLocation()
        }
        //getCurrentLocation()

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_LOCATION_CODE-> if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                                                setUpMap()
                                    }else  Toast.makeText(this,"Access Denined",Toast.LENGTH_LONG).show()
                                //  enableLocation()
        }
    }

    fun  enableLocation(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@GoogleMapsActivity)
        alertDialog.setTitle("Enable Location")
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")
        alertDialog.setPositiveButton("Settings",
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            })
        alertDialog.show()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        progressBarMaps.visibility = View.VISIBLE
        val locationRequest:LocationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){
                override fun onLocationResult(locationresult: LocationResult) {
                    super.onLocationResult(locationresult)

                    LocationServices.getFusedLocationProviderClient(this@GoogleMapsActivity)
                        .removeLocationUpdates(this)
                        if (locationresult.locations != null && locationresult.locations.size > 0){
                            val latestLocationIndex:Int = locationresult.locations.size - 1
                            val latitude:Double = locationresult.locations.get(latestLocationIndex).latitude
                            val longitude:Double = locationresult.locations.get(latestLocationIndex).longitude

                            val latLng = LatLng(latitude,longitude)
                            drawMarker(latLng)


                            tvLatitude.text = latitude.toString()
                            tvLongitude.text = longitude.toString()
                            tvAddress.text = getAddress(latLng.latitude,latLng.longitude)
                            progressBarMaps.visibility = View.GONE


                            val location:Location = Location("providerNA")
                            location.latitude = latitude
                            location.longitude = longitude
                           // fetchAddressFromLatLong(location)
                            //placeMarkerOnMap(latLng)

                        }else{
                            progressBarMaps.visibility = View.GONE

                        }
                }
            }, Looper.getMainLooper())

    }

    fun fetchAddressFromLatLong(location: Location){



        val intent :Intent = Intent(this,FetchAddressIntentService::class.java)
        intent.putExtra(Constants.RECEIVER,resultReceiver)
        intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA,location)
        startService(intent)
    }

    /*inner class AddressResultReceiver : ResultReceiver {

        constructor(handler: Handler) : super(handler)

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            if(resultCode == Constants.SUCCESS_RESULT){
                tvAddress.text = resultData!!.getString(Constants.RESULT_DATA_KEY)

            }else{
                Toast.makeText(this@GoogleMapsActivity,resultData!!.getString(Constants.RESULT_DATA_KEY),Toast.LENGTH_LONG).show()
            }
            progressBarMaps.visibility = View.GONE

        }*/


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        drawMarker(latLng)

        mMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {
                if (currentMarker != null) currentMarker?.remove()

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

        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        currentMarker = mMap?.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }
    fun getAddress(latitude: Double, longitude: Double):String?{
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude,longitude,1)
        return  addresses[0].getAddressLine(0).toString()
    }

    private fun setUpMap() {

        if(ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_CODE)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener{ location ->
            if (location != null){
                this.currentLocation = location
                val mapFragment = supportFragmentManager.findFragmentById(R.id.Mymap) as SupportMapFragment
                mapFragment.getMapAsync(this)


            }

        }
    }

    private fun placeMarkerOnMap(latLng: LatLng) {
        val makeroption = MarkerOptions().position(latLng)
        makeroption.title("$latLng")
        mMap?.addMarker(makeroption)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }




}




