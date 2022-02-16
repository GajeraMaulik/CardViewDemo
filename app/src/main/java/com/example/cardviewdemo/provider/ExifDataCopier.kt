package com.example.cardviewdemo.provider

import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File

object ExifDataCopier {
    fun copyExif(filePathOri: Uri, filePathDest: File) {
        try {
            val oldExif = ExifInterface(filePathOri.path!!)
            val newExif = ExifInterface(filePathDest)
            val attributes: List<String> = listOf(
                "FNumber",
                "ExposureTime",
                "ISOSpeedRatings",
                "GPSAltitude",
                "GPSAltitudeRef",
                "FocalLength",
                "GPSDateStamp",
                "WhiteBalance",
                "GPSProcessingMethod",
                "GPSTimeStamp",
                "DateTime",
                "Flash",
                "GPSLatitude",
                "GPSLatitudeRef",
                "GPSLongitude",
                "GPSLongitudeRef",
                "Make",
                "Model",
                "Orientation"
            )
            for (attribute in attributes) {
                setIfNotNull(oldExif, newExif, attribute)
            }
            newExif.saveAttributes()
        } catch (ex: Exception) {
            Log.e("ExifDataCopier", "Error preserving Exif data on selected image: $ex")
        }
    }

    private fun setIfNotNull(oldExif: ExifInterface, newExif: ExifInterface, property: String) {
        if (oldExif.getAttribute(property) != null) {
            newExif.setAttribute(property, oldExif.getAttribute(property))
        }
    }
}
