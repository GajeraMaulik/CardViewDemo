package com.example.cardviewdemo.services.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task

class FirebaseSignUpInstance {
    private val mAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = null
    var firebaseUsers = MutableLiveData<FirebaseUser?>()
    fun signInUser(
        userNameSignIn: String?,
        emailSignIn: String?,
        passwordSignIn: String?
    ): MutableLiveData<Task<*>> {
        val taskSignIn = MutableLiveData<Task<*>>()
        mAuth.createUserWithEmailAndPassword(emailSignIn!!, passwordSignIn!!)
            .addOnCompleteListener { value ->
                firebaseUser = mAuth.currentUser
                firebaseUsers.value = firebaseUser
                taskSignIn.value = value
            }
        return taskSignIn
    }
}