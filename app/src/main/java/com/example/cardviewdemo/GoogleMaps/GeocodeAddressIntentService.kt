package com.example.cardviewdemo.GoogleMaps

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log
import com.example.cardviewdemo.constants.Constants
import java.io.IOException
import java.util.*


class GeocodeAddressIntentService: IntentService(null) {
    protected var resultReceiver: ResultReceiver? = null
    private val TAG = "GEO_ADDY_SERVICE"


    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "onHandleIntent")
        val geocoder = Geocoder(this, Locale.getDefault())
        var errorMessage = ""
        var addresses: List<Address>? = null

        val fetchType = intent!!.getIntExtra(Constants.FETCH_TYPE_EXTRA, 0)
        Log.e(TAG, "fetchType == $fetchType")

        if (fetchType == Constants.USE_ADDRESS_NAME) {
            val name = intent!!.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA)
            try {
                addresses = geocoder.getFromLocationName(name, 1)
            } catch (e: IOException) {
                errorMessage = "Service not available"
                Log.e(TAG, errorMessage, e)
            }
        } else if (fetchType == Constants.USE_ADDRESS_LOCATION) {
            val latitude = intent!!.getDoubleExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, 0.0)
            val longitude = intent!!.getDoubleExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, 0.0)
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1)
            } catch (ioException: IOException) {
                errorMessage = "Service Not Available"
                Log.e(TAG, errorMessage, ioException)
            } catch (illegalArgumentException: IllegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used"
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + latitude + ", Longitude = " +
                        longitude, illegalArgumentException)
            }
        } else {
            errorMessage = "Unknown Type"
            Log.e(TAG, errorMessage)
        }

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER)
        if (addresses == null || addresses.size == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Not Found"
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null)
        } else {
            for (address in addresses) {
                var outputAddress = ""
                for (i in 0 until address.getMaxAddressLineIndex()) {
                    outputAddress += " --- " + address.getAddressLine(i)
                }
                Log.e(TAG, outputAddress)
            }
            val address: Address = addresses[0]
            val addressFragments: ArrayList<String?> = ArrayList()
            for (i in 0 until address.getMaxAddressLineIndex()) {
                addressFragments.add(address.getAddressLine(i))
            }
            Log.i(TAG, "Address Found")
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                TextUtils.join(System.getProperty("line.separator"),
                    addressFragments), address)
        }
    }
    private fun deliverResultToReceiver(resultCode: Int, message: String, address: Address?) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.RESULT_ADDRESS, address)
        bundle.putString(Constants.RESULT_DATA_KEY, message)
        resultReceiver!!.send(resultCode, bundle)
    }

}