package com.example.cardviewdemo.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.chat.UsersActivity
import com.example.cardviewdemo.constants.Constants
import com.example.cardviewdemo.databinding.ActivitySignInBinding
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*


class SignInActivity() : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var username: String
    private lateinit var password: String
    private var firebaseAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var ref: DatabaseReference? = null
    private var prg: ProgressDialog? = null
    lateinit var logInViewModel: LogInViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getUserSession()
        listener()

    }
    private fun init() {
        logInViewModel = LogInViewModel()
        logInViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[LogInViewModel::class.java]
        val firebaseDatabase = FirebaseDatabase.getInstance()

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


        firebaseAuth = FirebaseAuth.getInstance()
        ref = firebaseDatabase.reference
        ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cardviewdemo-4027f-default-rtdb.firebaseio.com/")
        prg = ProgressDialog(this)

        etUser.setBackgroundResource(R.drawable.edittext_selector)
        etPassword.setBackgroundResource(R.drawable.edittext_selector)

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
                resetPassword(etUser.text.toString())
                /*         firebaseAuth?.sendPasswordResetEmail(etUser.text.toString())
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

                             }*/
            }
        }


    }

    private fun resetPassword(email: String) {
        logInViewModel.addPasswordResetEmail(email)
        logInViewModel.successPasswordReset!!.observe(this) { task ->
            if (!task.isSuccessful) {
                etUser.isClickable = true
                etUser.setText("")
                val error: String? =
                    Objects.requireNonNull(task.exception)?.message
                etUser.requestFocus()
                Toast.makeText(this@SignInActivity, error, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SignInActivity,
                    "Please check your Email.",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun listener() {

        val buttonClick = AlphaAnimation(1f, 0.8f)
        signinBtn.setOnClickListener(View.OnClickListener { v: View ->
            etUser.clearFocus()
            etPassword.clearFocus()
            v.startAnimation(buttonClick)
            dismissKeyboard()
            username = etUser.text.toString().trim()
            password = etPassword.text.toString()
            if (password.isEmpty() && username.isEmpty()) {
                Toast.makeText(this@SignInActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
                etUser.requestFocus()
            } else if (username.isEmpty()) {
                etUser.error = "Please enter your Email Id."
                etUser.requestFocus()
            } else if (password.isEmpty()) {
                etPassword.error = "Please enter your password."
                etPassword.requestFocus()
            } else {
               // scrollViewLogin.setClickable(false)
                etUser.isClickable = false
                etPassword.isClickable = false
                etUser.isClickable = false
                signupBtn.isClickable = false
               // frameLayoutLogin.setVisibility(View.VISIBLE)
                logInUser()
            }
          //  isValid()
        })
        signupBtn.setOnClickListener {
            val intent = Intent(baseContext, SignUpActivity::class.java)
            startActivity(intent)
           // overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()
        }
    }

    private fun isValid(): Boolean {
        var invalid = true
        username = etUser.text.toString().trim()
        password = etPassword.text.toString()
        dismissKeyboard()
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
                logInUser()
             //   login(username, password)

            }
        }
        return invalid

    }

    private fun login(email: String, password: String) {
        logInViewModel.userLogIn(email, password)
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
                         //   Constants.user = username
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



    fun logInUser() {
        logInViewModel.userLogIn(username, password)
        logInViewModel.logInUser!!.observe(this) { task ->
            if (!task.isSuccessful) {
                //frameLayoutLogin.setVisibility(View.GONE)
              //  scrollViewLogin.setClickable(true)
                etUser.isClickable = true
                etPassword.isClickable = true
                etUser.isClickable = true
               // textToSignUp.setClickable(true)
                etUser.setText("")
                etPassword.setText("")
                etUser.requestFocus()
                try {
                    throw Objects.requireNonNull(task.exception)!!
                } catch (invalidEmail: FirebaseAuthInvalidUserException) {
                    Toast.makeText(this@SignInActivity,
                        "Invalid credentials, please try again.",
                        Toast.LENGTH_SHORT).show()
                } catch (wrongPassword: FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@SignInActivity,
                        "Wrong password or username , please try again.",
                        Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@SignInActivity,
                        "Check Internet Connection.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
              //  val intent = Intent(this@SignInActivity, UsersActivity::class.java)
               /* SharePref.save(this@SignInActivity,"isLogin",true)
                SharePref.save(this@SignInActivity,"username", username)*/
                SharePref.save(this, "isLogin", true)
                SharePref.save(this, "User", username)
                val intent = Intent(this@SignInActivity, UsersActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("username", username)
                startActivity(intent)

                finish()

            }
        }
    }

    private fun getUserSession() {
        logInViewModel.firebaseUserLogInStatus
        logInViewModel.firebaseUserLoginStatus.observe(this) { firebaseUser ->
            currentUser = firebaseUser
            if (currentUser != null) {
                val intent = Intent(this@SignInActivity, UsersActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    fun dismissKeyboard() {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}














