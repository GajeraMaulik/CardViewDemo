package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.adapter.UserAdapter
import com.example.cardviewdemo.data.Message
import com.example.cardviewdemo.data.UserProfile
import com.example.cardviewdemo.databinding.ActivityChatBinding
import com.example.cardviewdemo.login.SignInActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

 private lateinit var binding : ActivityChatBinding
 private  lateinit var userList : ArrayList<UserProfile>
 private  lateinit var mAuth : FirebaseAuth
 @SuppressLint("StaticFieldLeak")
 private  lateinit var adapter: UserAdapter
 private  lateinit var mDbRef:DatabaseReference
class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar= supportActionBar
        val user = SharePref.getStringValue(this,"User")

        actionBar!!.title= user
        actionBar.setDisplayHomeAsUpEnabled(false)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        rvUserView.adapter = adapter

        mDbRef.child("Users").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postsnapshot in snapshot.children){
                    Log.d("data","$postsnapshot")
                    val currentUser = postsnapshot.getValue(UserProfile::class.java)

                    if (mAuth.currentUser?.uid != currentUser?.Uid){
                        userList.add(currentUser!!)

                    }
                }
                adapter.notifyDataSetChanged()

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
            mAuth.signOut()
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
  /*  override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
  /*  private fun initFirebase() {
        //init firebase
        FirebaseApp.initializeApp(applicationContext)

//        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
    }
    private fun createFirebaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val toReturn: ArrayList<Message> = ArrayList();

                for(data in dataSnapshot.children){
                    val messageData = data.getValue<Message>(Message::class.java)

                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }

                //sort so newest at bottom
                toReturn.sortBy { message ->
                    message.timestamp
                }

                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }
    private fun setupAdapter(data: ArrayList<Message>){
        val linearLayoutManager = LinearLayoutManager(this)
        rvChatView.layoutManager = linearLayoutManager
        rvChatView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.message} clicked", Toast.LENGTH_SHORT).show()
        }

        //scroll to bottom
        rvChatView.scrollToPosition(data.size - 1)
    }
    private fun setupSendButton() {
        sendButton.setOnClickListener {
            if (!mainActivityEditText.text.toString().isEmpty()){
                sendData()
            }else{
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendData(){
        databaseReference?.
        child("messages")?.
        child(java.lang.String.valueOf(System.currentTimeMillis()))?.
        setValue(Message(mainActivityEditText.text.toString()))

        //clear the text
        mainActivityEditText.setText("")
    }
*/

}