package com.softeer.team6four.ui.user.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginSuccessState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginSuccessState: StateFlow<Boolean> = _loginSuccessState

    private val _loginFailState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginFailState: StateFlow<Boolean> = _loginFailState
    fun updateEmail(text: Editable) {
        _email.value = text.toString()
    }

    fun updatePassword(text: Editable) {
        _password.value = text.toString()
    }

    fun requestLogin() {
        viewModelScope.launch {
            userRepository.requestLogin(email.value, password.value).collect { userLoginModel ->
                when (userLoginModel) {
                    is Resource.Success -> {
                        _loginSuccessState.value = true
                        _loginFailState.value = false
                        Log.d("userLoginModel", userLoginModel.data.toString())
                    }

                    is Resource.Error -> {
                        Log.e("LogIn error", userLoginModel.message)
                        _loginSuccessState.value = false
                        _loginFailState.value = true
                    }

                    else -> {
                        _loginSuccessState.value = false
                        _loginFailState.value = false
                    }

                }

            }
        }
    }
}