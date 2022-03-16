package com.example.cardviewdemo.services.notifications
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.content.SharedPreferences
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.MessagingActivity
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("StaticFieldLeak")
private lateinit var context:Context

class MessagingServices : FirebaseMessagingService(), LifecycleOwner {
    var logInViewModel: LogInViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
        application).create(
        LogInViewModel::class.java)

    val CHANNEL_ID = "my_notification_channel"
    companion object{
        var sharedPref: SharedPreferences? = null
        //var sharePref = SharePref

        var token:String?
            get(){
                //  return SharePref.getStringValue(context,"token")
                return sharedPref?.getString("token","")
            }
            set(value){
                // SharePref.save(context,"token",value!!)
                sharedPref?.edit()?.putString("token",value)?.apply()
            }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val mAuth = FirebaseAuth.getInstance()
        val firebaseUser = mAuth.currentUser

        //  token = p0
        if (null != firebaseUser) {
            updateToken(p0)
        }
    }
    private fun updateToken(newToken: String) {
        logInViewModel.updateToken(newToken)
        logInViewModel.successUpdateToken?.observe(this as LifecycleOwner,
            Observer<Boolean?> { })
    }
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val sent: String? = p0.data["sent"]
        val user: String? = p0.data["user"]

        val preferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val currentUser = preferences.getString("currentuser", "none")

        val mAuth = FirebaseAuth.getInstance()
        val firebaseUser = mAuth.currentUser

        assert(sent != null)
        if (firebaseUser != null && sent == firebaseUser.uid) {
            if (currentUser != user) {
                if (Build.VERSION.SDK_INT > O) {
                    sendOreoNotification(p0)
                }
                sendNotification(p0)
            }
        }
        /*  Log.d("FCM Response", "FCM Message received")
          val intent = Intent(this,UsersActivity::class.java)
          val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          val notificationId = Random.nextInt()

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
              createNotificationChannel(notificationManager)
          }

          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
          val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
          val notification = NotificationCompat.Builder(this,CHANNEL_ID)
              .setContentTitle(p0.data["user"])
              .setContentText(p0.data["message"])
              .setSmallIcon(R.drawable.ic_baseline_notifications_24)
              .setAutoCancel(true)
              .setContentIntent(pendingIntent)
              .build()

          notificationManager.notify(notificationId,notification)*/
    }

    /*     @RequiresApi(Build.VERSION_CODES.O)
         private fun createNotificationChannel(notificationManager: NotificationManager){

             val channelName = "ChannelFirebaseChat"
             val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
                 description="MY FIREBASE CHAT DESCRIPTION"
                 enableLights(true)
                 lightColor = Color.WHITE
             }
             notificationManager.createNotificationChannel(channel)

         }*/


    @RequiresApi(O)
    private fun sendOreoNotification(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data["user"]
        val icon = remoteMessage.data["icon"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val notification = remoteMessage.notification
        assert(user != null)
        val j = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MessagingActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreoNotification = OreoNotification(this)
        val builder: Notification.Builder =
            oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon)
        var i = 0
        if (j > 0) {
            i = j
        }
        oreoNotification.manager.notify(i, builder.build())
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data["user"]
        val icon = remoteMessage.data["icon"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val notification = remoteMessage.notification
        assert(user != null)
        val j = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MessagingActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        assert(icon != null)
        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var i = 0
        if (j > 0) {
            i = j
        }
        notificationManager.notify(i, builder.build())
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }


    /*  var logInViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
          application).create(
          LogInViewModel::class.java)

      override fun onNewToken(s: String) {
          super.onNewToken(s)
          val mAuth = FirebaseAuth.getInstance()
          val firebaseUser = mAuth.currentUser
          if (null != firebaseUser) {
              updateToken(s)
          }
      }

      private fun updateToken(newToken: String) {
          logInViewModel.updateToken(newToken)
          logInViewModel.successUpdateToken!!.observe((this as LifecycleOwner)
          ) { }
      }

      override fun onMessageReceived(remoteMessage: RemoteMessage) {
          super.onMessageReceived(remoteMessage)
          val sent = remoteMessage.data["sent"]
          val user = remoteMessage.data["user"]
          val preferences = getSharedPreferences("PREFS", MODE_PRIVATE)
          val currentUser = preferences.getString("currentuser", "none")
          val mAuth = FirebaseAuth.getInstance()
          val firebaseUser = mAuth.currentUser
          assert(sent != null)
          if (firebaseUser != null && sent == firebaseUser.uid) {
              if (currentUser != user) {
                  if (Build.VERSION.SDK_INT > O) {
                      sendOreoNotification(remoteMessage)
                  }
                  sendNotification(remoteMessage)
              }
          }
      }

      @RequiresApi(api = O)
      private fun sendOreoNotification(remoteMessage: RemoteMessage) {
          val user = remoteMessage.data["user"]
          val icon = remoteMessage.data["icon"]
          val title = remoteMessage.data["title"]
          val body = remoteMessage.data["body"]
          val notification = remoteMessage.notification
          assert(user != null)
          val j = user!!.replace("[\\D]".toRegex(), "").toInt()
          val intent = Intent(this, MessagingActivity::class.java)
          val bundle = Bundle()
          bundle.putString("userId", user)
          intent.putExtras(bundle)
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
          val pendingIntent = PendingIntent.getActivity(this, j, intent, FLAG_ONE_SHOT)
          val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
          val oreoNotification = OreoNotification(this)
          val builder =
              oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon)
          var i = 0
          if (j > 0) {
              i = j
          }
          oreoNotification.manager.notify(i, builder.build())
      }

      private fun sendNotification(remoteMessage: RemoteMessage) {
          val user = remoteMessage.data["user"]
          val icon = remoteMessage.data["icon"]
          val title = remoteMessage.data["title"]
          val body = remoteMessage.data["body"]
          val notification = remoteMessage.notification
          assert(user != null)
          val j = user!!.replace("[\\D]".toRegex(), "").toInt()
          val intent = Intent(this, MessagingActivity::class.java)
          val bundle = Bundle()
          bundle.putString("userId", user)
          intent.putExtras(bundle)
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
          val pendingIntent = PendingIntent.getActivity(this, j, intent, FLAG_ONE_SHOT)
          val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
          assert(icon != null)
          val builder = NotificationCompat.Builder(this)
              .setSmallIcon(R.mipmap.ic_launcher)
              .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                  R.mipmap.ic_launcher))
              .setContentTitle(title)
              .setContentText(body)
              .setAutoCancel(true)
              .setSound(defaultSound)
              .setContentIntent(pendingIntent)
          val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
          var i = 0
          if (j > 0) {
              i = j
          }
          notificationManager.notify(i, builder.build())
      }

      override fun getLifecycle(): Lifecycle {
          return lifecycle
      }*/
}