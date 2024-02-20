package com.softeer.team6four.ui.user.signup

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.user.UserRepository
import com.softeer.team6four.data.remote.user.model.UserInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    private val password: StateFlow<String> = _password

    private val _confirmPassword: MutableStateFlow<String> = MutableStateFlow("")
    private val confirmPassword: StateFlow<String> = _confirmPassword

    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _emailCheckState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val emailCheckState: StateFlow<Boolean> = _emailCheckState

    private val _emailErrorState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val emailErrorState: StateFlow<Boolean> = _emailErrorState

    private val _nicknameCheckState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val nicknameCheckState: StateFlow<Boolean> = _nicknameCheckState

    private val _nicknameErrorState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val nicknameErrorState: StateFlow<Boolean> = _nicknameErrorState

    private val _signUpState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val signUpState: StateFlow<Boolean> = _signUpState

    fun updateEmail(text: Editable) {
        _email.value = text.toString()
        _emailCheckState.value = false
        _emailErrorState.value = false
    }

    fun updatePassword(text: Editable) {
        _password.value = text.toString()
    }

    fun updateConfirmPassword(text: Editable) {
        _confirmPassword.value = text.toString()
    }

    fun updateNickname(text: Editable) {
        _nickname.value = text.toString()
        _nicknameCheckState.value = false
        _nicknameErrorState.value = false
    }

    val passwordSameCheckState: StateFlow<Boolean> =
        password.combine(confirmPassword) { password, confirmPassword ->
            password != confirmPassword
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val nextBtnState: StateFlow<Boolean> =
        emailCheckState.combine(passwordSameCheckState) { emailState, passwordState ->
            emailState && !passwordState
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun checkDuplicateEmail() {
        viewModelScope.launch {
            userRepository.fetchEmailCheck(email.value).catch { e ->
                Log.e("duplicate email check", e.toString())
            }.collect { emailCheck ->
                when (emailCheck) {
                    is Resource.Success -> {
                        _emailCheckState.value = !emailCheck.data.checkResult
                        _emailErrorState.value = emailCheck.data.checkResult
                    }

                    is Resource.Error -> {
                        if (emailCheck.code == 400) {
                            _emailCheckState.value = false
                            _emailErrorState.value = true
                        } else {
                            Log.e("unknown error", emailCheck.message)
                        }
                    }

                    else -> {
                        _emailCheckState.value = false
                        _emailErrorState.value = false
                    }
                }
            }
        }
    }

    fun checkDuplicateNickname() {
        viewModelScope.launch {
            userRepository.fetchNicknameCheck(nickname.value).catch { e ->
                Log.e("Fail nickname check", e.toString())
            }.collect { nicknameCheck ->
                when (nicknameCheck) {
                    is Resource.Success -> {
                        _nicknameCheckState.value = !nicknameCheck.data.checkResult
                        _nicknameErrorState.value = nicknameCheck.data.checkResult
                    }

                    is Resource.Error -> {
                        if (nicknameCheck.code == 400) {
                            _nicknameCheckState.value = false
                            _nicknameErrorState.value = true
                        } else {
                            Log.e("unknown error", nicknameCheck.message)
                        }
                    }

                    else -> {
                        _nicknameErrorState.value = false
                        _nicknameCheckState.value = false
                    }
                }
            }
        }
    }

    fun requestSignUp() {
        viewModelScope.launch {
            userRepository.requestSignUp(
                UserInfoModel(
                    email = email.value,
                    password = password.value,
                    nickname = nickname.value
                )
            ).collect { result ->
                if (result is Resource.Success) {
                    _signUpState.value = true
                }
                else if (result is Resource.Error) {
                    Log.e("requestSignUp Result", result.message)
                }
            }
        }
    }

}