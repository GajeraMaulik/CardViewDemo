package com.example.cardviewdemo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cardviewdemo.services.repository.FirebaseLoginInstance
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogInViewModel : ViewModel() {
    private var loginInstance = FirebaseLoginInstance()

    @JvmField
    var logInUser: LiveData<Task<*>>? = null
    lateinit var firebaseUserLoginStatus: LiveData<FirebaseUser>
    lateinit var firebaseAuthLiveData: LiveData<FirebaseAuth>
    @JvmField
    var successPasswordReset: LiveData<Task<*>>? = null
    @JvmField
    var successUpdateToken: LiveData<Boolean>? = null

    fun userLogIn(emailLogIn: String?, pwdLogIn: String?) {
        logInUser = loginInstance.loginUser(emailLogIn, pwdLogIn)
    }


    init{
        loginInstance = FirebaseLoginInstance()
    }


    val firebaseUserLogInStatus: Unit
        get() {
            firebaseUserLoginStatus = loginInstance.firebaseUserLoginStatus
        }
    val firebaseAuth: Unit
        get() {
            firebaseAuthLiveData = loginInstance.firebaseAuth
        }

    fun addPasswordResetEmail(email: String?) {
        successPasswordReset = loginInstance.resetPassword(email)
    }

    fun updateToken(newToken: String?) {
        successUpdateToken = loginInstance.successUpdateToken(newToken)
    }

}