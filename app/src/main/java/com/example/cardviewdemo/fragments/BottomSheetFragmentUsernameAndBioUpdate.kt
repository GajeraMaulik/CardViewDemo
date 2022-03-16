package com.example.cardviewdemo.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomSheetFragmentUsernameAndBioUpdate :
    BottomSheetDialogFragment(){
    var isUsername: Boolean? = null
    var et_user_input_bottom_sheet_fragment: EditText? = null
    var btnSave: TextView? = null
    var btnCancel: TextView? = null
    lateinit var databaseViewModel: DatabaseViewModel
    var username: String? = null
    var bio: String? = null

    fun BottomSheetFragmentUsernameAndBioUpdate(context: Context?, isUsername: Boolean?) {
        val context = context
        this.isUsername = isUsername
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_bottom_sheet_bio_username_update, container, false)
        Objects.requireNonNull(Objects.requireNonNull<Dialog>(dialog).window)!!
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        view.findViewById<View>(R.id.et_user_input_bottom_sheet_fragment).requestFocus()
        init(view)
        return view
    }

    private fun updateUsernameAndBio() {
        if (isUsername == true) {
            username = et_user_input_bottom_sheet_fragment?.text.toString().trim { it <= ' ' }
            databaseViewModel.addUsernameInDatabase("username", username)
        } else {
            bio = et_user_input_bottom_sheet_fragment?.text.toString().trim { it <= ' ' }
            databaseViewModel.addBioInDatabase("bio", bio)
        }
        dismiss()
    }

    private fun init(view: View) {
        databaseViewModel = DatabaseViewModel()
        et_user_input_bottom_sheet_fragment =
            view.findViewById<EditText>(R.id.et_user_input_bottom_sheet_fragment)
        btnSave = view.findViewById<TextView>(R.id.btn_save_bottom_sheet)
        btnCancel = view.findViewById<TextView>(R.id.btn_cancel_bottom_sheet)
        btnSave?.setOnClickListener(View.OnClickListener { updateUsernameAndBio() })
        btnCancel?.setOnClickListener(View.OnClickListener { dismiss() })
    }
}