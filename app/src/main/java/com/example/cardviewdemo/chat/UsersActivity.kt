package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.UserAdapter
import com.example.cardviewdemo.services.model.UserProfile
import com.example.cardviewdemo.databinding.ActivityUsersBinding
import com.example.cardviewdemo.login.SignInActivity
import com.example.cardviewdemo.login.username
import com.example.cardviewdemo.services.MessagingServices
import com.example.cardviewdemo.services.model.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessaging.getInstance
import kotlinx.android.synthetic.main.activity_users.*
import java.util.*
import kotlin.collections.ArrayList

private lateinit var binding : ActivityUsersBinding
 private  lateinit var userList : ArrayList<UserProfile>
 private   var mAuth : FirebaseAuth? = null
private   var mDbRef:DatabaseReference? = null
private   var mDatabase:FirebaseDatabase? = null
private  lateinit var userAdapter: UserAdapter
 private  lateinit var firebaseUser: FirebaseUser
 @SuppressLint("StaticFieldLeak")
class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar= supportActionBar
        val user = SharePref.getStringValue(this,"User")

        val uid = intent.getStringExtra("Uid")

        actionBar!!.title= user
        actionBar.setDisplayHomeAsUpEnabled(false)


        MessagingServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
           FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MAulik", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
               Log.d("TAG","FCM$token")
            MessagingServices.token = token
        })
        userList = ArrayList()
        userAdapter = UserAdapter(this, userList)
        binding.rvUserView.adapter = userAdapter



       /*     firebaseUser = mAuth?.currentUser!!
            mDbRef = mDatabase?.getReference("Users")?.child(firebaseUser.uid)

        mDbRef?.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val user  = snapshot.getValue(UserProfile::class.java)
                actionBar.title = user?.getusername()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })*/

       getUserList()

    }
    private fun getUserList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val userId = firebase.uid
            getInstance().subscribeToTopic("/topics/$userId")
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef!!.child("Users").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postsnapshot in snapshot.children){
                    Log.d("data","DATA$postsnapshot")
                    val currentUser = postsnapshot.getValue(UserProfile::class.java)

                    if (mAuth!!.currentUser?.uid != currentUser?.Uid ){
                        userList.add(currentUser!!)

                    }
                }
                binding.rvUserView.adapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            mAuth?.signOut()
            SharePref.removeSharePref(this)
            val intent = Intent(this,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

}