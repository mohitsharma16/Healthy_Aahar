//package com.mohit.healthy_aahar.ui.viewmodel
//
//import android.app.Activity
//import androidx.lifecycle.ViewModel
//import com.mohit.healthy_aahar.repository.AuthRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val authRepository: AuthRepository
//) : ViewModel() {
//
//    fun signInWithGoogle(activity: Activity, onResult: (Boolean) -> Unit) {
//        val intent = authRepository.getSignInIntent()
//        activity.startActivityForResult(intent, 9001)
//    }
//
//    fun signOut() {
//        authRepository.signOut()
//    }
//
//    fun getUser() = authRepository.getUser()
//}