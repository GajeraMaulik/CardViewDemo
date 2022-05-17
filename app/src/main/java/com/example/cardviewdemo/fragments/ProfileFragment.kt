package com.example.cardviewdemo.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.android.gms.tasks.Continuation
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

 class ProfileFragment : Fragment() {
    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var dataImageByte: ByteArray

    lateinit var tv_profile_fragment_username :TextView
    lateinit var tv_currentUserName_profile_fragment: TextView
    lateinit var iv_profileImage_profile_fragment: CircleImageView
    lateinit var btn_profile_image_change: ImageView
    lateinit var btn_save_edit_user_name: ImageView
    lateinit var tv_profile_fragment_bio: TextView
    var username: String? = null
    var imageUrl: String? = null
    var userBio: String? = null
    private var imageUri: Uri? = null
    var timeStamp: String? = null
    private var uploadImageTask: StorageTask<*>? = null
    private var fileReference: StorageReference? = null
    var isUsername: Boolean? = null



     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context : Context

        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        init(view)
        fetchCurrentUserdata()
        return view
    }

    private fun fetchCurrentUserdata() {
        databaseViewModel.fetchingUserDataCurrent()
        databaseViewModel.fetchUserCurrentData?.observe(viewLifecycleOwner) { dataSnapshot ->
            val user: Users = dataSnapshot.getValue(Users::class.java)!!
            username = user.getUsername()
            imageUrl = user.getImageUrl()
            userBio = user.getBio()
            tv_profile_fragment_username.text = username
            tv_profile_fragment_bio.text = userBio
            if (imageUrl == "default") {
                iv_profileImage_profile_fragment.setImageResource(R.drawable.sample_img)
            } else {
                Glide.with(this).load(imageUrl).into(iv_profileImage_profile_fragment)
            }
        }
    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading Image.")
        progressDialog.show()


        if (imageUri != null) {
            val formatter = SimpleDateFormat("dd-mm", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now).toString() + ".jpeg"
            val tsLong = System.currentTimeMillis()
            timeStamp = tsLong.toString()
            databaseViewModel.fetchImageFileReference(filename, imageUri, context)
            databaseViewModel.imageFileReference?.observe(this
            ) { storageReference ->
                fileReference = storageReference
                uploadImageTask = fileReference!!.putBytes(dataImageByte) //image address
                (uploadImageTask as UploadTask).continueWithTask(Continuation { task ->
                    if (!task.isSuccessful) {
                        throw Objects.requireNonNull(task.exception)!!
                    }
                    fileReference!!.downloadUrl
                }) .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downLoadUri = task.result!!
                        val mUri = downLoadUri.toString()
                        databaseViewModel.addImageUrlInDatabase("imageUrl", mUri)
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()
                }.addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
        } else {
            Toast.makeText(context, "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            var bmp : Bitmap
           bmp = MediaStore.Images.Media.getBitmap(Activity().contentResolver,imageUri)

            try {
                 bmp = MediaStore.Images.Media.getBitmap(Activity().contentResolver,imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val baos = ByteArrayOutputStream()

                bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos) //compression
                dataImageByte = baos.toByteArray()


            if (uploadImageTask != null && uploadImageTask!!.isInProgress) {
                Toast.makeText(context, "Upload in progress.", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }
    }

    private fun openBottomSheet(isUsername: Boolean) {
        val bottomSheetFragmentUsernameAndBioUpdate = BottomSheetFragmentUsernameAndBioUpdate()
        bottomSheetFragmentUsernameAndBioUpdate.BottomSheetFragmentUsernameAndBioUpdate(context,isUsername)
        assert(fragmentManager != null)
        bottomSheetFragmentUsernameAndBioUpdate.show(requireFragmentManager(), "edit")
    }

    private fun init(view: View) {
        databaseViewModel = DatabaseViewModel()
/*        databaseViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(Objects.requireNonNull(Activity()).application))
            .get(DatabaseViewModel::class.java)
        tv_currentUserName_profile_fragment =
            view.findViewById<TextView>(R.id.tv_profile_fragment_username)*/
        iv_profileImage_profile_fragment = view.findViewById(R.id.iv_profile_fragment)
        tv_profile_fragment_username= view.findViewById(R.id.tv_profile_fragment_username)
        btn_profile_image_change = view.findViewById(R.id.btn_profile_image_change)
        btn_save_edit_user_name = view.findViewById(R.id.btn_save_edit_username)
        tv_profile_fragment_bio = view.findViewById<TextView>(R.id.tv_profile_fragment_bio)
        btn_profile_image_change.setOnClickListener(View.OnClickListener { openImage() })
        iv_profileImage_profile_fragment.setOnClickListener(View.OnClickListener { openImage() })
        btn_save_edit_user_name!!.setOnClickListener(View.OnClickListener {
            isUsername = true
            openBottomSheet(true)
        })
        tv_profile_fragment_bio!!.setOnClickListener(View.OnClickListener {
            isUsername = false
            openBottomSheet(false)
        })
    }

    companion object {
        private const val IMAGE_REQUEST = 1
    }
}
