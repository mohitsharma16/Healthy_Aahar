//package com.mohit.healthy_aahar.repository
//
//import android.content.Intent
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class AuthRepository @Inject constructor(
//    private val auth: FirebaseAuth,
//    private val googleSignInClient: GoogleSignInClient
//) {
//    fun getUser() = auth.currentUser
//
//    fun signOut() {
//        auth.signOut()
//        googleSignInClient.signOut()
//    }
//
//    fun getSignInIntent(): Intent {
//        return googleSignInClient.signInIntent
//    }
//
//    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                onResult(task.isSuccessful)
//            }
//    }
//}
