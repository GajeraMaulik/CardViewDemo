package com.example.cardviewdemo.Login

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Log.d
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cardviewdemo.R
import com.example.cardviewdemo.databinding.ActivitySignUpBinding
import com.example.cardviewdemo.Services.model.Users
import com.example.cardviewdemo.ViewModel.DatabaseViewModel
import com.example.cardviewdemo.ViewModel.SignInViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.etPassword
import kotlinx.android.synthetic.main.activity_sign_up.ivEye
import java.util.*

lateinit var username : String

open class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private  var prg : ProgressDialog? = null
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var email: String
    private lateinit var password : String
    private lateinit var databaseReference :DatabaseReference
    private  lateinit var database : FirebaseDatabase
     private var currentUser:FirebaseUser?=null
    private lateinit var userprofile : Users
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var signInViewModel: SignInViewModel
    var userId: String? = null
    var imageUrl: String? = null
    var timeStamp: String? = null
    private var databaseViewModel= DatabaseViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar= supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)
//        currentUser = FirebaseAuth.getInstance().currentUser!!

        init()
        listeners()




    }

    private fun init() {
        etEmailUp.setBackgroundResource(R.drawable.edittext_selector)
        etUserName.setBackgroundResource(R.drawable.edittext_selector)
        etPassword.setBackgroundResource(R.drawable.edittext_selector)
        signInViewModel = SignInViewModel()

        firebaseAuth = FirebaseAuth.getInstance()
        prg = ProgressDialog(this)
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
        signinBtnUp.setOnClickListener {
            val intent = Intent(baseContext, SignInActivity::class.java)
            startActivity(intent)
            // overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()
        }
    }

    fun listeners() {
        registerBtn.setOnClickListener {
                etUserName.clearFocus()
                etEmailUp.clearFocus()
                etPassword.clearFocus()
                username = etUserName.text.toString().trim()
                email  = etEmailUp.text.toString().trim()
                password = etPassword.text.toString().trim()
                if (password.isEmpty() && email.isEmpty() && username.isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Fields are empty!", Toast.LENGTH_SHORT)
                        .show()
                    etUserName.requestFocus()
                } else if (username.isEmpty()) {
                    etUserName.error = "Please enter a username."
                    etUserName.requestFocus()
                } else if (email.isEmpty()) {
                    etEmailUp.error = "Please enter your Email Id."
                    etEmailUp.requestFocus()
                } else if (password.isEmpty()) {
                    etPassword.error = "Please set your password."
                    etPassword.requestFocus()
                } else {
                    etUserName.isClickable = false
                    etEmailUp.isClickable = false
                    etPassword.isClickable = false
                    signinBtnUp.isClickable = false
                    dismissKeyboard()
                    signInUsers()
                }
            //isValid()
            }

    }


private fun isValid(): Boolean {
        var invalid = true
          username = etUserName.text.toString().trim()
          email  = etEmailUp.text.toString().trim()
          password = etPassword.text.toString().trim()
        prg?.setMessage("Please wait...")
        prg?.show()

        if (username.isEmpty()) {
            invalid = false
            Toast.makeText(applicationContext, "Enter your Username", Toast.LENGTH_SHORT).show()
            etUserName.requestFocus()
            prg?.dismiss()
        }
        else if (email.isEmpty()){
            invalid=false
            Toast.makeText(applicationContext, "Enter your Email", Toast.LENGTH_SHORT).show()
            etEmailUp.requestFocus()
            prg?.dismiss()
            //  etEmail.setError(getResources().getString(R.string.email_error));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(etEmailUp.text.toString()).matches()  ) {
            invalid=false
            etEmailUp.error = resources.getString(R.string.error_invalid_email)
            etEmailUp.requestFocus()
            prg?.dismiss()

        }
        else if (password.isEmpty()) {
            invalid = false
            Toast.makeText(applicationContext, "Enter your Password", Toast.LENGTH_SHORT).show()
            etPassword.requestFocus()
            prg?.dismiss()
            //  etPassword.error = resources.getString(R.string.password_error)
        }
        else if (password.length <= 7){
            invalid=false
            etPassword.error=resources.getString(R.string.error_invalid_password)
            etPassword.requestFocus()
            prg?.dismiss()

        }
        else if (!checkString(password)){
            invalid=false
            etPassword.error="Password must contain at least 8 characters;must contain alphanumeric;must contain One Capital alphabet."
            etPassword.requestFocus()
            prg?.dismiss()

        }
        else {
            invalid = true
            etUserName.error = null
            etEmailUp.error =null
            etPassword.error= null
            dismissKeyboard()
            signInUsers()

           // signUp(username,email,password)

        }
        return invalid
    }

     fun signInUsers() {
         signInViewModel.userSignIn(username, email, password)
         signInViewModel.signInUser.observe(this,
             Observer<Task<*>> { task ->
                 if (!task.isSuccessful) {
                     etUserName.isClickable = true
                     etEmailUp.isClickable = true
                     etPassword.isClickable = true
                     registerBtn.isClickable = true
                    // progressBarSignInFrame.setVisibility(View.GONE)
                     etEmailUp.setText("")
                     etPassword.setText("")
                     etUserName.setText("")
                     etUserName.requestFocus()
                     try {
                         throw Objects.requireNonNull(task.exception)!!
                     } catch (existEmail: FirebaseAuthUserCollisionException) {
                         Toast.makeText(this, "Email Id already exists.", Toast.LENGTH_SHORT)
                             .show()
                     } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                         Toast.makeText(this,
                             "Password length should be more then six characters.",
                             Toast.LENGTH_SHORT).show()
                     } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                         Toast.makeText(this,
                             "Invalid credentials, please try again.",
                             Toast.LENGTH_SHORT).show()
                     } catch (e: java.lang.Exception) {
                         Toast.makeText(this,
                             "SignUp unsuccessful. Try again.",
                             Toast.LENGTH_SHORT).show()
                     }
                 } else {
                    getUserSession()
                     addUserInDatabase(username, email)
                     val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                     startActivity(intent)
                     finish()
                 }
             })
    }
     fun dismissKeyboard() {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

      fun addUserInDatabase(userName: String, email: String) {
        val tsLong = System.currentTimeMillis()
        timeStamp = tsLong.toString()
        imageUrl = "default"
        userId = currentUser?.uid
       //  Senddata(userId,userName,email,timeStamp)
        databaseViewModel.addUserDatabase(userId.toString(), userName, email, timeStamp.toString(),imageUrl.toString())
          databaseViewModel.successAddUserDb?.observe(this,
              Observer<Boolean?> { aBoolean ->
                  if (aBoolean) Toast.makeText(this, "", Toast.LENGTH_SHORT).show() else {
                      Toast.makeText(this, "ERROR WHILE ADDING DATA IN DATABASE.", Toast.LENGTH_SHORT).show()
                  }
              })
    }

     fun getUserSession() {
        signInViewModel.getUserFirebaseSession()
        signInViewModel.userFirebaseSession.observe(this,
            Observer { firebaseUser ->
                if (firebaseUser != null) {
                    currentUser = firebaseUser
                }
            })
    }


    private fun signUp(username:String,email: String,password:String){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener  { task ->

            if (task.isSuccessful ) {
               prg?.dismiss()
                Senddata(username,email,firebaseAuth.currentUser?.uid!!,password)
                //   d("TAG","add$add")
                startActivity(Intent(this, SignInActivity::class.java))

                Toast.makeText(this, "Successfully Registration$username", Toast.LENGTH_LONG).show()
                d("TAG", "Successfully Registration\nemail: $email\n username: $username\n password: $password")

                finish()
               /* user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        firebaseAuth.fetchSignInMethodsForEmail(etEmailUp.text.toString()).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                VerifyEmail()

                                //  Log.d("TAG","Email not valid")
                                //Toast.makeText(this, "${task.exception?.message}" + username, Toast.LENGTH_LONG).show()

                            }else{
                                Log.d("TAG","Email Exits")

                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }

                    }else{

                        try {
                            throw task.exception!!
                        } catch (e: Exception) {
                            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }*/

            }else{
                prg?.dismiss()
                Log.d("TAG","Email Exits")
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun  VerifyEmail(): Boolean{
        val firebaseUser : FirebaseUser? = firebaseAuth.currentUser
        currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                task.exception?.message?.let { d("TAG", it) }
                //     startActivity(Intent(this,SignInActivity::class.java ))
                // finish()

            }else{
                try {
                    throw task.exception!!
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

            }
        }
        return true
    }

    private fun checkString(str: String): Boolean {
        var ch: Char
        var capitalFlag = false
        var lowerCaseFlag = false
        var numberFlag = false
        for (element in str) {
            ch = element

            when {
                Character.isDigit(ch) -> numberFlag = true
                Character.isUpperCase(ch) -> capitalFlag = true
                Character.isLowerCase(ch) -> lowerCaseFlag = true
            }

            if (numberFlag && capitalFlag && lowerCaseFlag) return true
        }
        return false
    }
/*

    private fun addUsertoDatabase(username:String,email:String,uid:String){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cardviewdemo-4027f-default-rtdb.firebaseio.com/")
        databaseReference.child("Users").child(uid).setValue(Users(username,email,uid))
    }

     private fun  VerifyEmail(): Boolean{
        val firebaseUser : FirebaseUser? = firebaseAuth.currentUser
         firebaseUser?.sendEmailVerification()?.addOnCompleteListener { task ->
             if (task.isSuccessful){
                 Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                 task.exception?.message?.let { d("TAG", it) }
                 //     startActivity(Intent(this,SignInActivity::class.java ))
                // finish()

             }else{
                 try {
                     throw task.exception!!
                 } catch (e: Exception) {
                     Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                 }

             }
         }
        return true
     }
*/

      private fun Senddata(  userId: String?, userName: String, emailId: String, timestamp: String?): Boolean {
           //userprofile = Users(username,email,password)
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(username)
          databaseReference.child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  when {
                      snapshot.hasChild(username) -> {
                          Toast.makeText(this@SignUpActivity,"User already Exits", Toast.LENGTH_LONG).show()
                          etUserName.requestFocus()
                          d("TAG","User already Exits")
                          prg?.dismiss()
                      }
                      snapshot.hasChild("$username/Email") -> {
                          Toast.makeText(this@SignUpActivity,"Email already Exits", Toast.LENGTH_LONG).show()
                          etEmailUp.requestFocus()
                          prg?.dismiss()
                      }
                      else -> {
                      //    databaseReference.child("Users").child(uid).setValue(UserProfile(username,email,uid))
                         // SharePref.save(this@SignUpActivity,"Username",username)
                          val hashMap:HashMap<String,String> = HashMap()
                          hashMap["id"] = userId!!
                          hashMap["username"] = userName
                          hashMap["emailId"] = emailId
                          hashMap["timestamp"] = timestamp!!
                          hashMap["status"] = "offline"
                          databaseReference.setValue(hashMap).addOnCompleteListener {
                              if (it.isSuccessful){
                                  etUserName.text = null
                                  etEmailUp.text = null
                                  etPassword.text = null
                              }
                          }
                       /*  databaseReference.child("Users").child(username).child("Username").setValue(username)
                          databaseReference.child("Users").child(username).child("Email").setValue(email)
                          databaseReference.child("Users").child(username).child("Uid").setValue(uid)
                          databaseReference.child("Users").child(username).child("Pass").setValue(password)*/
                          Toast.makeText(this@SignUpActivity,"User Successfully Registration", Toast.LENGTH_LONG).show()
                          prg?.dismiss()

                          d("TAG", "email: $email\nusername: $username")
                          d("TAG","senddate")
                      }
                  }
              }
              override fun onCancelled(error: DatabaseError) {
                error.message
              }

          })
          return true
    }
}





