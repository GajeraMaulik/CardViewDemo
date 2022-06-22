package com.example.cardviewdemo.constants

class Constants {

    companion object{
        lateinit var user:String


        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAAkDonE_M:APA91bFvZOr69axqTEKhdlkP95_XPTDDJ7MqhfX1iM6QiyAfFyWdEGnzImOMmSU4IVQh8DnXpr36o5KYXGZp5-hRX2lZJkN-WVm3lx_dcyPtVDkkgZ5qjWTif5vwnGrGcbJL0_glZuOo" // get firebase server key from firebase project setting
        const val CONTENT_TYPE = "application/json"
        const val OAUTH_TOKEN = "ya29.A0ARrdaM9AXWcYKIWyvDtmmnw2lgWzGywxuxTzaxnDKg6CKFBbgsFOj60GeIR47U4YbLGecv16I96fGaJTjJkj26win8UvU9B9CrazE8C5x24wlVZ0YWDVbuJXdLnmZ8h5RS8m28VheEUQI2wNB2Yh69atXZr5"

        const val URL = "https://timesofindia.indiatimes.com/"

        const val SUCCESS_RESULT = 1
        const val FAILURE_RESULT = 0

        const val USE_ADDRESS_NAME = 1
        const val USE_ADDRESS_LOCATION = 2

        const val PACKAGE_NAME = "com.example.cardviewdemo"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
        const val RESULT_ADDRESS = "$PACKAGE_NAME.RESULT_ADDRESS"
        const val LOCATION_LATITUDE_DATA_EXTRA =
            "$PACKAGE_NAME.LOCATION_LATITUDE_DATA_EXTRA"
        const val LOCATION_LONGITUDE_DATA_EXTRA =
            "$PACKAGE_NAME.LOCATION_LONGITUDE_DATA_EXTRA"
        const val LOCATION_NAME_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_NAME_DATA_EXTRA"
        const val FETCH_TYPE_EXTRA = "$PACKAGE_NAME.FETCH_TYPE_EXTRA"

    }

}