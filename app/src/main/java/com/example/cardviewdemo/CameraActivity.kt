package com.example.cardviewdemo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.cardviewdemo.databinding.ActivityCameraBinding
import com.example.cardviewdemo.util.FileUtil
import com.example.cardviewdemo.util.IntentUtils
import com.example.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.content_camera_only.*
import kotlinx.android.synthetic.main.content_gallery_only.*
import kotlinx.android.synthetic.main.content_profile.*


private lateinit var binding:ActivityCameraBinding

class CameraActivity : AppCompatActivity() {


    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null



    private val profileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == RESULT_OK ) {
                val uri = it.data?.data!!
                mProfileUri = uri
                Log.d("TAG","Profile:$uri")
                imgProfile.setLocalImage(uri, true)
                SharePref.save(this,"profile","$uri")
             /*   if (mProfileUri == null){
                    val empty = imgProfile.setImageResource(R.drawable.ic_person).toString()
                    SharePref.save(this,"pnull",empty)
                }*/

            } else parseError(it)

        }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK  ) {
                val uri = it.data?.data!!
                mGalleryUri = uri
                SharePref.save(this,"gallery","$uri")

                Log.d("TAG","Gallery:$uri")
                imgGallery.setLocalImage(uri)
            }
            else parseError(it)
        }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK ) {
                val uri = it.data?.data!!
                mCameraUri = uri
                Log.d("TAG","Camera: $uri")
                imgCamera.setLocalImage(uri, false)
                SharePref.save(this,"camera","$uri")
            } else parseError(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar= supportActionBar
        actionBar!!.title="Camera"
        actionBar.setDisplayHomeAsUpEnabled(true)


      if(imgProfile != null || imgCamera != null || imgGallery != null){
//         mProfileUri = Uri.parse(SharePref.getStringValue(this,"profile"))
          mGalleryUri = Uri.parse(SharePref.getStringValue(this,"gallery"))
          mCameraUri = Uri.parse(SharePref.getStringValue(this,"camera"))
          imgProfile.setLocalImage(Uri.parse(mProfileUri.toString()), true)
          imgGallery.setLocalImage(Uri.parse(mGalleryUri.toString()))
          imgCamera.setLocalImage(Uri.parse(mCameraUri.toString()))
        } else {}
          val view = imgProfile.setImageResource(R.drawable.ic_person)
          SharePref.save(this, "profile", "$view")

         /* val profile: Uri? = Uri.parse(SharePref.getStringValue(this, "profile"))
            imgProfile.setDrawableImage(Uri.parse(profile.toString()), true)
            Log.d("TAG", "p:$profile")*/




    }


    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun pickGalleryImage(view: View) {
        galleryLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .cropFreeStyle()
                .galleryMimeTypes( // no gif images at all
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .createIntent()
        )
    }

    fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            .crop()
            .cropOval()
            .cropFreeStyle()
            .maxResultSize(512, 512, true)
            .createIntentFromDialog { profileLauncher.launch(it) }
    }
    fun pickCameraImage(view: View) {
        cameraLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .cameraOnly()
               .cropFreeStyle()
               .maxResultSize(1080, 1920, true)
                .createIntent()
        )
    }
    fun showImage(view: View) {
        val uri = when (view) {
            imgProfile ->
                mProfileUri

            imgCamera ->
                mCameraUri

            imgGallery ->
                mGalleryUri

            else -> null
        }


        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(this, uri))
        }
    }

    fun showImageInfo(view: View) {
        val uri = when (view) {
            imgProfileInfo -> mProfileUri
            imgCameraInfo-> mCameraUri
            imgGalleryInfo-> mGalleryUri
            else -> null
        }

        AlertDialog.Builder(this)
            .setTitle("Image Info")
            .setMessage(FileUtil.getFileInfo(this, uri ))
            .setPositiveButton("Ok", null)
/*            .setNegativeButton("Delete", DialogInterface.OnClickListener { view,uri ->
                when{
                    imgProfile != null -> {
                      //  SharePref.removeSharePref(this)
                       val view = imgProfile.setImageResource(R.drawable.ic_person)
                        SharePref.save(this, "profile", "$view")

                    }
                    imgCamera != null -> {
                        SharePref.removeSharePref(this)
                        SharePref.save(this,"camera","")

                    }
                    imgGallery != null -> {
                        SharePref.removeSharePref(this)
                        SharePref.save(this,"gallery","")
                    }
                }
            })*/
            .show()
    }

}



/* private fun chooseImageGallery() {
     val intent = Intent(Intent.ACTION_PICK)
     intent.type = "image/*"
     startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
 }

 override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
     super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     when (requestCode) {
         1001 -> {

             if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                 chooseImageGallery()
                 takePicture()

             } else {
                 Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
             }
             return
         }

         else -> {

         }
     }
 }

 private fun requestPermission() {
     ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA), PERMISSION_REQUEST_CODE)
 }

 private fun takePicture() {
     val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

 *//*    if (intent.resolveActivity(this.packageManager) != null){
         startActivityForResult(intent,PERMISSION_REQUEST_CODE)
     }else{
         Toast.makeText(this,"Unable to open camera",Toast.LENGTH_LONG).show()
     }*//*
     file =  createFile(FILE_NAME)
     Log.d("TAG","$file")
 //    intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
     val fileProvider = FileProvider.getUriForFile(this, "com.example.cardviewdemo.provider", file)
 *//*    binding.videoView.setVideoURI(Uri.parse("com.example.cardviewdemo.provider" ,file))
     binding.videoView.start()
     binding.videoView.setOnErrorListener { mp, what, extra ->
         Toast.makeText(applicationContext, "An Error Occured " +
                 "While Playing Video !!!", Toast.LENGTH_LONG).show()
         false
     }*//*

     intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
     startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
 }
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     when(requestCode){
         REQUEST_IMAGE_CAPTURE ->
             if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

                 //To get the File for further usage
                 val auxFile = File("data")

                 // val image = data?.extras?.get("data") as Bitmap
                 val image = BitmapFactory.decodeFile(file.absolutePath)
                 binding.imageView.setImageBitmap(image)
             }else{
                 super.onActivityResult(requestCode, resultCode, data)

             }
         OPERATION_CHOOSE_PHOTO ->
             if (resultCode == Activity.RESULT_OK) {
                 if (Build.VERSION.SDK_INT >= 32) {
                     handleImageOnKitkat(data)
                 }
             }

     }

 }
 @TargetApi(19)
 private fun handleImageOnKitkat(data: Intent?) {
     var imagePath: String? = null
     val uri = data!!.data
     //DocumentsContract defines the contract between a documents provider and the platform.
     if (DocumentsContract.isDocumentUri(this, uri)){
         val docId = DocumentsContract.getDocumentId(uri)
         if ("com.example.cardviewdemo.provider" == uri?.authority){
             val id = docId.split(":")[1]
             val selsetion = MediaStore.Images.Media._ID + "=" + id
             imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                 selsetion)
         }
         else if ("com.example.cardviewdemo.provider" == uri?.authority){
             val contentUri = ContentUris.withAppendedId(Uri.parse(
                 "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
             imagePath = getImagePath(contentUri, null)
         }
     }
     else if ("content".equals(uri?.scheme, ignoreCase = true)){
         imagePath = getImagePath(uri, null)
     }
     else if ("file".equals(uri?.scheme, ignoreCase = true)){
         imagePath = uri?.path
     }
     renderImage(imagePath)
 }

 private fun renderImage(imagePath: String?){
     if (imagePath != null) {
         val bitmap = BitmapFactory.decodeFile(imagePath)
         binding.imageView.setImageBitmap(bitmap)
     }
     else {
         Toast.makeText(this,"ImagePath is null",Toast.LENGTH_LONG).show()
     }
 }
 @SuppressLint("Range")
 private fun getImagePath(uri: Uri?, selection: String?): String {
     var path: String? = null
     val cursor = uri?.let { contentResolver.query(it, null, selection, null, null ) }
     if (cursor != null){
         if (cursor.moveToFirst()) {
             path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
         }
         cursor.close()
     }
     return path!!
 }

 private fun checkPersmission(): Boolean {
     return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
             PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
         android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
 }


 @SuppressLint("SimpleDateFormat")
 @Throws(IOException::class)
 private fun createFile(filename : String): File {
     // Create an image file name
     val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
     val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)


     return File.createTempFile(filename, *//* prefix *//*".jpg", *//* suffix *//*storageDir *//* directory *//*).apply {
         // Save a file: path for use with ACTION_VIEW intents
         mCurrentPhotoPath = absolutePath
     }
 }*/
}

 */

