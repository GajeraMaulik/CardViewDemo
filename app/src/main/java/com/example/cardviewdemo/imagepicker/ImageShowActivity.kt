package com.example.cardviewdemo.imagepicker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cardviewdemo.adapter.ImageAdapter
import com.example.cardviewdemo.services.model.Image
import com.example.cardviewdemo.databinding.ActivityImageShowBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_image_show.*

private lateinit var binding : ActivityImageShowBinding



class ImageShowActivity : AppCompatActivity() {
    private val TAG = "WallpaperPermission"
    private val MY_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar= supportActionBar
        actionBar!!.title="Images Fetching"
        actionBar.setDisplayHomeAsUpEnabled(true)

        makeRequest()
        setupPermissions()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("Images")
        val imageList: ArrayList<Image> = ArrayList()
        progressBar.visibility = View.VISIBLE

        val listAllTask: Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items

            if (items.isNotEmpty()){
                //add cycle for add image url to list
                items.forEachIndexed  { index, item ->
                    item.downloadUrl.addOnSuccessListener {
                        Log.d("item", "$it")
                        imageList.add(Image(it.toString()))

                    }.addOnCompleteListener {

                        progressBar.visibility = View.INVISIBLE
                        noData.visibility = View.INVISIBLE
                        rvLoadImage.adapter = ImageAdapter(imageList, this)

                        //   rvLoadImage.layoutManager = LinearLayoutManager(this)
                    }
                }

            }else{
                Log.d("item","Fail")
                progressBar.visibility = View.INVISIBLE
                noData.visibility = View.VISIBLE
            }

        }




        /*    binding.getImage.setOnClickListener {
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
            }*/
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "Permission to SET_WALLPAPER denied.")
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SET_WALLPAPER),
            MY_REQUEST_CODE)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}