package com.example.cardviewdemo.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class BottomSheetProfileDetailUser(var username: String, var imageURL: String, var bio: String) : BottomSheetDialogFragment() {
    lateinit var iv_profile_bottom_sheet_profile_image: CircleImageView
    private var tv_profile__bottom_sheet_fragment_username: TextView? = null
    private var tv_profile_bottom_sheet_fragment_bio: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_bottom_sheet_show_profile, container, false)
        Objects.requireNonNull(Objects.requireNonNull<Dialog>(dialog).window)?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        init(view)
        setDetails(username, imageURL, bio)
        return view
    }

    private fun setDetails(username: String, imageURL: String, bio: String) {
        tv_profile__bottom_sheet_fragment_username?.text = username
        tv_profile_bottom_sheet_fragment_bio!!.text = bio
        if (imageURL == "default") {
            iv_profile_bottom_sheet_profile_image.setImageResource(R.drawable.sample_img)
        } else {
            Glide.with(this).load(imageURL).into(iv_profile_bottom_sheet_profile_image)
        }
    }

    private fun init(view: View) {
        iv_profile_bottom_sheet_profile_image = view.findViewById(R.id.iv_profile_bottom_sheet)
        tv_profile__bottom_sheet_fragment_username =
            view.findViewById<TextView>(R.id.tv_profile__bottom_sheet_fragment_username)
        tv_profile_bottom_sheet_fragment_bio =
            view.findViewById<TextView>(R.id.tv_profile_bottom_sheet_fragment_bio)
    }
}