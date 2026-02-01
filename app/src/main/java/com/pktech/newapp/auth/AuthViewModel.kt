package com.pktech.newapp.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    val authSuccess = MutableLiveData<Boolean>()

    fun loginWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authSuccess.postValue(true) }
            .addOnFailureListener {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authSuccess.postValue(true) }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authSuccess.postValue(true) }
    }
}