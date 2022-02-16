package com.example.cardviewdemo.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.cardviewdemo.R
import com.example.cardviewdemo.constant.ImageProvider
import com.example.cardviewdemo.listener.ResultListener


internal object DialogHelper {

    fun showChooseAppDialog(context: Context, listener: ResultListener<ImageProvider>) {
        val layoutInflater = LayoutInflater.from(context)
        val view:View = layoutInflater.inflate(R.layout.dialog_choose_app, null)


        val galleryPick=view.findViewById<LinearLayout>(R.id.galleryPick)
        val cameraPick = view.findViewById<LinearLayout>(R.id.cameraPick)
        val dialog = AlertDialog.Builder(context)
            .setTitle(R.string.title_choose_image_provider)
            .setView(view)
            .setOnCancelListener {
                listener.onResult(null)
            }
            .setNegativeButton(R.string.action_cancel) { _, _ ->
                listener.onResult(null)
            }
            .show()

        // Handle Camera option click
        cameraPick.setOnClickListener {
            listener.onResult(ImageProvider.CAMERA)
            dialog.dismiss()
        }

        // Handle Gallery option click
        galleryPick.setOnClickListener {
            listener.onResult(ImageProvider.GALLERY)
            dialog.dismiss()
        }
    }
}
