package com.mohit.healthy_aahar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.healthy_aahar.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val isSignup: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            repository.login(email, password) { success, message ->
                if (success) {
                    _authState.value = AuthState.Success(isSignup = false)
                } else {
                    _authState.value = AuthState.Error(message ?: "Login failed")
                }
            }
        }
    }

    fun signup(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            repository.signup(email, password) { success, message ->
                if (success) {
                    _authState.value = AuthState.Success(isSignup = true)
                } else {
                    _authState.value = AuthState.Error(message ?: "Signup failed")
                }
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }
}
