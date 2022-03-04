package com.example.cardviewdemo.services
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.chat.UsersActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
@SuppressLint("StaticFieldLeak")
private lateinit var context:Context

class MessagingServices : FirebaseMessagingService() {

        val CHANNEL_ID = "my_notification_channel"
        companion object{
            //var sharedPref:SharedPreferences? = null
            var sharePref = SharePref

            var token:String?
                get(){
                    return SharePref.getStringValue(context,"token")
                  //  return sharedPref?.getString("token","")
                }
                set(value){
                    SharePref.save(context,"token",value!!)
                   // sharedPref?.edit()?.putString("token",value)?.apply()
                }
        }

        override fun onNewToken(p0: String) {
            super.onNewToken(p0)
            token = p0
        }

        override fun onMessageReceived(p0: RemoteMessage) {
            super.onMessageReceived(p0)

            val intent = Intent(this,UsersActivity::class.java)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = Random.nextInt()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2){
                createNotificationChannel(notificationManager)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
            val notification = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(p0.data["title"])
                .setContentText(p0.data["message"])
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notificationId,notification)
        }

        @RequiresApi(Build.VERSION_CODES.S_V2)
        private fun createNotificationChannel(notificationManager: NotificationManager){

            val channelName = "ChannelFirebaseChat"
            val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
                description="MY FIREBASE CHAT DESCRIPTION"
                enableLights(true)
                lightColor = Color.WHITE
            }
            notificationManager.createNotificationChannel(channel)

        }
    }
