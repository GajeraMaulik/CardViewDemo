package com.example.cardviewdemo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardviewdemo.services.repository.FirebaseSignUpInstance
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser


class SignInViewModel : ViewModel() {
    private lateinit var signUpInstance : FirebaseSignUpInstance

    var signInUser: LiveData<Task<*>>? = null
    lateinit var userFirebaseSession: MutableLiveData<FirebaseUser?>

    fun SignInViewModel() {
        signUpInstance = FirebaseSignUpInstance()
    }

    fun userSignIn(userNameSignIn: String?, emailSignIn: String?, passwordSignIn: String?) {
        signInUser = signUpInstance.signInUser(userNameSignIn, emailSignIn, passwordSignIn)
    }


    fun getUserFirebaseSession() {
        userFirebaseSession = signUpInstance.firebaseUsers
    }
}