package com.example.cardviewdemo.GoogleMaps

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import com.example.cardviewdemo.constants.Constants
import java.util.*
import kotlin.collections.ArrayList

class FetchAddressIntentService : IntentService("FetchAddressIntentService") {


    lateinit var resultReceiver: ResultReceiver



    override fun onHandleIntent(intent: Intent?) {

        if (intent != null){
            var errorMessage:String? = null
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER)!!
            val location: Location = intent.getParcelableExtra(Constants.LOCATION_NAME_DATA_EXTRA)?: return
            val geocoder:Geocoder = Geocoder(this, Locale.getDefault())
            var addresses : List<Address>?= null
            try {
                 addresses = geocoder.getFromLocation(location.latitude,location.longitude,1)
            }catch (e:Exception){
                errorMessage = e.message
            }
            if (addresses == null ){
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage.toString())
            }else{
                val address = addresses.get(0)
                val addressFraments : ArrayList<String> = ArrayList()
                    for (i in 0..address.maxAddressLineIndex){
                        addressFraments.add(address.getAddressLine(i))
                    }
                    deliverResultToReceiver(Constants.SUCCESS_RESULT,TextUtils.join(
                        Objects.requireNonNull(System.getProperty("line.separator")),addressFraments
                    ))
            }
        }
    }

    fun deliverResultToReceiver(resultcode:Int,addressMessage:String){
        val bundle: Bundle = Bundle()
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage)
        resultReceiver.send(resultcode,bundle)
    }

}