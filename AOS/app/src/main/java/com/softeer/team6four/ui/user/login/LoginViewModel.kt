package com.softeer.team6four.ui.user.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.UserPreferencesRepository
import com.softeer.team6four.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password


    private val _loginEmailFailState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginEmailFailState: StateFlow<Boolean> = _loginEmailFailState

    private val _loginPasswordFailState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginPasswordFailState: StateFlow<Boolean> = _loginPasswordFailState

    private var _loginState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

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
                        _loginState.value = true
                        _loginEmailFailState.value = false
                        _loginPasswordFailState.value = false

                        with(userPreferencesRepository) {
                            updateNickname(userLoginModel.data.nickname)
                            updateAccessToken(userLoginModel.data.accessToken)
                            updateRefreshToken(userLoginModel.data.refreshToken)
                        }
                    }

                    is Resource.Error -> {
                        Log.e("LogIn error", userLoginModel.message)
                        _loginState.value = false
                        if (userLoginModel.code == 404) {
                            _loginEmailFailState.value = true
                            _loginPasswordFailState.value = false
                        } else {
                            _loginEmailFailState.value = false
                            _loginPasswordFailState.value = true
                        }

                    }

                    else -> {
                        _loginState.value = false
                        _loginEmailFailState.value = false
                        _loginPasswordFailState.value = false
                    }
                }
            }
        }
    }

    fun updateLoginState() {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken()
            accessToken.collect { token ->
                _loginState.value = token.isNotEmpty()
            }
            Log.d("accessToken", accessToken.first())
        }
    }

    fun initLoginResult() {
        _loginState.value = false
    }
}