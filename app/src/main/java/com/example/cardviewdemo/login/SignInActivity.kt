package com.example.cardviewdemo.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cardviewdemo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.chat.UsersActivity
import com.example.cardviewdemo.services.model.App
import com.example.cardviewdemo.services.model.UserProfile
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.etPassword
import kotlinx.android.synthetic.main.activity_sign_in.ivEye


class SignInActivity() : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var username: String
    private lateinit var password: String
    private var firebaseAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var ref: DatabaseReference? = null
    private var prg: ProgressDialog? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: UserProfile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseDatabase = FirebaseDatabase.getInstance()

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


        firebaseAuth = FirebaseAuth.getInstance()
        ref = firebaseDatabase.reference
        ref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://cardviewdemo-4027f-default-rtdb.firebaseio.com/")
        prg = ProgressDialog(this)

        etUser.setBackgroundResource(R.drawable.edittext_selector)
        etPassword.setBackgroundResource(R.drawable.edittext_selector)

        signinBtn.setOnClickListener {
            isValid()
            Log.d("TAG", "$username")
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()

        }
        var isVisiblePassword = false
        ivEye.setOnClickListener {

            if (!isVisiblePassword) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivEye.setImageResource(R.drawable.ic_visibility_on_eye)

                isVisiblePassword = true
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivEye.setImageResource(R.drawable.ic_visibility_off_eye)
                isVisiblePassword = false
            }
            etPassword.setSelection(etPassword.text.length)
        }
        forgotPassword.setOnClickListener {
            if (etUser.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Enter your Email", Toast.LENGTH_SHORT).show()
                etUser.requestFocus()
            } else {
                firebaseAuth?.sendPasswordResetEmail(etUser.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            currentUser = firebaseAuth?.currentUser
                            Toast.makeText(applicationContext,
                                "Reset Password Link Sent ${etUser.text}",
                                Toast.LENGTH_SHORT).show()
                        } else if (task.isComplete) {
                            databaseReference = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://tanamgroceryapp-default-rtdb.firebaseio.com/")
                            databaseReference.child("Users")
                            databaseReference.child(username).child("Password")
                                .setValue(currentUser?.uid)
                        } else {
                            Toast.makeText(applicationContext,
                                task.exception?.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }

    }


    private fun isValid(): Boolean {
        var invalid = true
        username = etUser.text.toString().trim()
        password = etPassword.text.toString()

        // val myRefernce = databaseReference?.child(currentuser?.uid!!)
        prg?.setMessage("Please wait...")
        prg?.show()
        when {
            username.isEmpty() -> {
                invalid = false
                Toast.makeText(applicationContext, "Enter your Username", Toast.LENGTH_SHORT).show()
                etUser.requestFocus()
                prg?.dismiss()
            }
            password.isEmpty() -> {
                invalid = false
                Toast.makeText(applicationContext, "Enter your Password", Toast.LENGTH_SHORT).show()
                etPassword.requestFocus()
                prg?.dismiss()
                //  etPassword.error = resources.getString(R.string.password_error)
            }
            password.length <= 7 -> {
                invalid = false
                etPassword.error = resources.getString(R.string.error_invalid_password)
                prg?.dismiss()

            }
            else -> {
                invalid = true
                etUser.error = null
                etPassword.error = null
                login(username, password)

            }
        }
        return invalid

    }

    private fun login(email: String, password: String) {
        /*   ref?.child("Users")!!.addListenerForSingleValueEvent(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  if (snapshot.hasChild(username)){

                      val getpassword:String = snapshot.child(username).child("Pass").value as String

                      if (getpassword == password){

                          Log.d("login Successfully", "Username: $username\n Password: $username")
                          SharePref.save(this@SignInActivity,"isLogin",true)
                          SharePref.save(this@SignInActivity,"username", username)

                          val i = Intent(this@SignInActivity, ChatActivity::class.java)
                          i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                          i.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                          startActivity(i);

                          Toast.makeText(this@SignInActivity,"Successfully login $username",Toast.LENGTH_LONG).show()

                          prg?.dismiss()
                          finish()


                      }else{
                          Log.d("TAG", "Wrong: $password\nusername1: $username")
                          Toast.makeText(this@SignInActivity,"Wrong password",Toast.LENGTH_LONG).show()
                          etPassword.error = "Incorrect Password"
                          etPassword.requestFocus()
                          prg?.dismiss()
                          Log.d("TAG", "email2: $username\n pass2: $username")
                      }

                  }else{
                      Log.d("TAG", "user2:user not found $username")
                      Toast.makeText(this@SignInActivity,"User Not Found",Toast.LENGTH_LONG).show()
                      etUser.error ="User Not Found"
                      etUser.requestFocus()
                      prg?.dismiss()
                  }
              }

              override fun onCancelled(error: DatabaseError) {
                  error.toException().message
              }

          })*/
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth!!.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            prg?.dismiss()
                            App.user = username
                            SharePref.save(this, "isLogin", true)
                            SharePref.save(this, "User", email)
                            val intent = Intent(this@SignInActivity, UsersActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("username", email)
                            finish()
                            startActivity(intent)
                            /* val user: FirebaseUser? = firebaseAuth!!.currentUser

                    if (user!!.isEmailVerified){

                    }else{
                        Log.d("TAG", "Please Verified Email")

                        Toast.makeText(this@SignInActivity, "Please Verified Email ", Toast.LENGTH_LONG).show()
                    }*/

                        } else {
                            prg?.dismiss()
                            Log.d("TAG", "${task.exception?.message}")

                            Toast.makeText(this@SignInActivity,
                                task.exception?.message,
                                Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                prg?.dismiss()
                Toast.makeText(this@SignInActivity, task.exception?.message, Toast.LENGTH_LONG)
                    .show()
            }

        }
    }
}














