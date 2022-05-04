package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.ViewPagerAdapter
import com.example.cardviewdemo.constants.Constants
import com.example.cardviewdemo.databinding.ActivityUsersBinding
import com.example.cardviewdemo.fragments.ChatFragment
import com.example.cardviewdemo.fragments.ProfileFragment
import com.example.cardviewdemo.fragments.UserFragment
import com.example.cardviewdemo.login.SignInActivity
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView


//private lateinit var binding
private   var mAuth : FirebaseAuth? = null
 lateinit var binding : ActivityUsersBinding

@SuppressLint("StaticFieldLeak")
class UsersActivity : AppCompatActivity() {
    private lateinit var logInViewModel: LogInViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var databaseViewModel: DatabaseViewModel
    lateinit var linearLayout: LinearLayout
    lateinit var progressBar: ProgressBar
    lateinit var database : FirebaseDatabase

    var currentUserName: TextView? = null
    private lateinit var profileImage: CircleImageView
    lateinit var currentFirebaseUser: FirebaseUser
    var username: String? = null
    var imageUrl: String? = null

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    val chatfragment =ChatFragment()
    val userFragment =UserFragment()
    val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_users)

        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // database= FirebaseDatabase.getInstance()
   // database.setPersistenceEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this,R.color.colorChat)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager,PagerAdapter.POSITION_UNCHANGED)




        init()
        getCurrentFirebaseUser()
        fetchCurrentUserdata()
        setupPagerFragment()
        onOptionMenuClicked()

    }

    private fun getCurrentFirebaseUser() {
        logInViewModel = LogInViewModel()
        logInViewModel.firebaseUserLogInStatus
        logInViewModel.firebaseUserLoginStatus.observe(this
        ) { firebaseUser ->
            currentFirebaseUser = firebaseUser
            userId_sender = currentFirebaseUser.uid
            intent.putExtra("currentuser", userId_sender)

        }

    }

    private fun setupPagerFragment() {

        viewPagerAdapter.addFragment(chatfragment, "Chats")
        viewPagerAdapter.addFragment(userFragment, "Users")
        viewPagerAdapter.addFragment(profileFragment, "Profile")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCurrentUserdata() {
        databaseViewModel.fetchingUserDataCurrent()
        databaseViewModel.fetchUserCurrentData?.observe(this
        ) { dataSnapshot ->
            val user: Users? = dataSnapshot.getValue(Users::class.java)
            if (user != null) {
                progressBar.visibility = View.GONE
                linearLayout.visibility = View.VISIBLE
                username = user.getUsername()
                imageUrl = user.getImageUrl()
                d("TAG","$username")
                 // Toast.makeText(this, "Welcome back $username.", Toast.LENGTH_SHORT).show();
                currentUserName?.text = username
                if (imageUrl == "default") {
                    profileImage.setImageResource(R.drawable.sample_img)
                } else {
                    Glide.with(applicationContext).load(imageUrl).into(profileImage)
                }
            } else {
                Toast.makeText(this@UsersActivity, "User not found..", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun init() {
        val actionBar= supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        progressBar = ProgressBar(this)
        tabLayout = TabLayout(this)
        linearLayout = LinearLayout(this)
        databaseViewModel = DatabaseViewModel()
        viewPager = ViewPager(this)
        toolbar = Toolbar(this)
        logInViewModel = LogInViewModel()
        logInViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[LogInViewModel::class.java]
        toolbar = findViewById(R.id.toolbar)
         ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[DatabaseViewModel::class.java]
        currentUserName = findViewById(R.id.tv_username)
        profileImage = findViewById(R.id.iv_profile_image)
        linearLayout = findViewById(R.id.linearLayout)
        progressBar = findViewById(R.id.progress_bar)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        profileImage.setOnClickListener(View.OnClickListener {
            viewPager.currentItem = 2 // to go to profile fragment
        })
    }
    private fun status(status: String) {
        databaseViewModel.addStatusInDatabase("status", status)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }
    /* private fun getUserList(){
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

      //  val userId = firebase.currentUser
        d("TAG","user: $currentUser")
            getInstance().subscribeToTopic("/topics/$currentUser")
        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        mDbRef!!.child("Users").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postsnapshot in snapshot.children){
                    Log.d("data","DATA$postsnapshot")
                    val currentUser:Users = postsnapshot.getValue(Users::class.java)!!

                    //if (currentUser!!.getEmailId() != null){
                   // if (currentUser != null) {
                        if (!mAuth!!.currentUser?.uid.equals(currentUser.getId())){

                            userList.add(currentUser)

                        }
                    }//
                  //  }

                binding.rvUserView.adapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
*/

     fun menuIconColor(menuItem: MenuItem, color: Int) {
        val drawable: Drawable = menuItem.icon
        drawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.DST_ATOP)
    }

    private fun getUserAuthToSignOut() {
        logInViewModel.firebaseAuth
        logInViewModel.firebaseAuthLiveData.observe(this
        ) { firebaseAuth ->
            firebaseAuth.signOut()
            val intent = Intent(this@UsersActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun onOptionMenuClicked() {
        toolbar.inflateMenu(R.menu.chat_menu)
        toolbar
        toolbar.setOnMenuItemClickListener { item ->
          //  menuIconColor(item,R.color.white)

            if (item.itemId == R.id.logout) {
                getUserAuthToSignOut()
                Toast.makeText(this@UsersActivity, "Logged out", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
  *//*          mAuth?.signOut()
            SharePref.removeSharePref(this)
            val intent = Intent(this,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
            startActivity(intent)*//*
                getUserAuthToSignOut()
            return true
        }
        return true
    }*/

}