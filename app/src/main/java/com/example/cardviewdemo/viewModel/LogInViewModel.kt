package com.example.cardviewdemo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.example.cardviewdemo.services.repository.FirebaseLoginInstance
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {
    private val loginInstance = FirebaseLoginInstance()

    @JvmField
    var logInUser: LiveData<Task<*>>? = null
    @JvmField
    var firebaseUserLoginStatus: LiveData<FirebaseUser>? = null
    var firebaseAuthLiveData: LiveData<FirebaseAuth>? = null
    @JvmField
    var successPasswordReset: LiveData<Task<*>>? = null
    @JvmField
    var successUpdateToken: LiveData<Boolean>? = null
    fun userLogIn(emailLogIn: String?, pwdLogIn: String?) {
        logInUser = loginInstance.loginUser(emailLogIn, pwdLogIn)
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