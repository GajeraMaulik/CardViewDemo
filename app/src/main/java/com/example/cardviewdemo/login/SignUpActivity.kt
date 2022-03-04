package com.example.cardviewdemo.login

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Log.d
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cardviewdemo.services.model.UserProfile
import com.example.cardviewdemo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import  com.example.cardviewdemo.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.etPassword
import kotlinx.android.synthetic.main.activity_sign_up.ivEye


open class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private  var prg : ProgressDialog? = null
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var username : String
    private lateinit var email: String
    private lateinit var password : String
    private lateinit var databaseReference :DatabaseReference
    private  lateinit var database : FirebaseDatabase
    var user:FirebaseUser? = null
    private lateinit var userprofile : UserProfile
    private lateinit var editor: SharedPreferences.Editor




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar= supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


        etEmailUp.setBackgroundResource(R.drawable.edittext_selector)
        etUserName.setBackgroundResource(R.drawable.edittext_selector)
        etPassword.setBackgroundResource(R.drawable.edittext_selector)

        firebaseAuth = FirebaseAuth.getInstance()
        prg = ProgressDialog(this)



        signinBtnUp.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        registerBtn.setOnClickListener {
         isValid()

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
        } /*else if (username.length <= 8) {
            invalid = false
            prg?.dismiss()
            etUserName.error ="Please enter a minimum 8 characters"
            etUserName.requestFocus()

        }*/
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
            signUp(username,email,password)

        }
        return invalid
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
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
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
    private fun addUsertoDatabase(username:String,email:String,uid:String){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cardviewdemo-4027f-default-rtdb.firebaseio.com/")
        databaseReference.child("Users").child(uid).setValue(UserProfile(username,email,uid))
    }

     /*private fun  VerifyEmail(): Boolean{
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

      private fun Senddata(username: String,email: String,uid: String,password: String): Boolean {
           userprofile = UserProfile(username,email,password)
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
                          val hashMap:HashMap<String,String> = HashMap()
                          hashMap["Username"] = username
                          hashMap["Email"] = email
                          hashMap["Uid"] = uid
                          hashMap["Pass"] = password

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




