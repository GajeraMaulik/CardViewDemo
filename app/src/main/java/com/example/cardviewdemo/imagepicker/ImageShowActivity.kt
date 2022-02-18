package com.example.cardviewdemo.imagepicker

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cardviewdemo.R
import com.example.cardviewdemo.databinding.ActivityCameraBinding
import com.example.cardviewdemo.databinding.ActivityImageShowBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

private lateinit var binding: ActivityImageShowBinding
class ImageShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getImage.setOnClickListener {
            val process = ProgressDialog(this)
            process.setMessage("Fetching image...")
            process.setCancelable(false)
            process.show()
            val imagename = binding.etImageId.text
            val storageRef = FirebaseStorage.getInstance().getReference("UserProfile/$imagename.jpeg")

            val localfile = File.createTempFile("tempImage","jpeg")
            storageRef.getFile(localfile).addOnSuccessListener {

                if (process.isShowing)
                    process.dismiss()
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
            }.addOnFailureListener{
                if (process.isShowing)
                    process.dismiss()
                Toast.makeText(this,"No data found", Toast.LENGTH_LONG).show()
            }
        }
    }
}