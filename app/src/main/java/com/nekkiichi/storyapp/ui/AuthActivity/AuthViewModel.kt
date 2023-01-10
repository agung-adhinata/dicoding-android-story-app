package com.nekkiichi.storyapp.ui.AuthActivity

import androidx.lifecycle.ViewModel

class AuthViewModel:ViewModel() {
    var email = ""
    var password = ""
    var repeatedPassword = ""

    fun logIn(email: String, password: String) {
        //TODO: login here
    }
    fun createNewAccount(email: String, password: String,repeatedPassword: String) {

    }

}