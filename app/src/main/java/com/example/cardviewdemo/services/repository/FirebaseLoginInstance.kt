package com.example.cardviewdemo.services.repository

import android.content.Context
import android.provider.Settings.Global.getString
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.MutableLiveData
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.notifications.Token
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.Task

class FirebaseLoginInstance {
    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseUser = mAuth.currentUser
    lateinit var  context:Context
    val firebaseUserLoginStatus : MutableLiveData<FirebaseUser>
        get() {
            val firebaseUserLoginStatus = MutableLiveData<FirebaseUser>()
            firebaseUserLoginStatus.value = firebaseUser
            return firebaseUserLoginStatus
        }
    val firebaseAuth: MutableLiveData<FirebaseAuth>
        get() {
            val firebaseAuth = MutableLiveData<FirebaseAuth>()
            firebaseAuth.value = mAuth
            return firebaseAuth
        }



    fun successUpdateToken(newToken: String?): MutableLiveData<Boolean> {
      //  val user = SharePref.getStringValue(context,"username")
        val successTokenUpdate = MutableLiveData<Boolean>()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(newToken)
        assert(firebaseUser != null)
        reference.child(firebaseUser?.uid!!).setValue(token)
        return successTokenUpdate
    }

    fun loginUser(emailLogin: String?, pwdLogin: String?): MutableLiveData<Task<*>> {
        val taskLogin = MutableLiveData<Task<*>>()
        mAuth.signInWithEmailAndPassword(emailLogin!!, pwdLogin!!)
            .addOnCompleteListener { task -> taskLogin.value = task }
        return taskLogin
    }

    fun resetPassword(email: String?): MutableLiveData<Task<*>> {
        val successResetPassword = MutableLiveData<Task<*>>()
        mAuth.sendPasswordResetEmail(email!!)
            .addOnCompleteListener { task -> successResetPassword.value = task }
        return successResetPassword
    }


}