package com.softeer.team6four.ui.user

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("android:enabled")
fun checkEmailEmpty(button: Button, text: String) {
    button.isEnabled = text.isNotEmpty()
}

@BindingAdapter("app:setEmailError")
fun setEmailErrorText(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError) textInputLayout.error = "중복되는 이메일입니다."
    else textInputLayout.error = null
}

@BindingAdapter("app:setEmailHelper")
fun setEmailHelperText(textInputLayout: TextInputLayout, isSuccess: Boolean) {
    if (isSuccess) textInputLayout.helperText = "사용가능한 이메일입니다."
    else textInputLayout.helperText = null
}

@BindingAdapter("app:setPasswordError")
fun setPasswordError(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError) textInputLayout.error = "비밀번호가 일치하지 않습니다."
    else textInputLayout.error = null
}

@BindingAdapter("app:setNicknameHelper")
fun setNicknameHelperText(textInputLayout: TextInputLayout, isSuccess: Boolean) {
    if (isSuccess) textInputLayout.helperText = "사용가능한 닉네임입니다."
    else textInputLayout.helperText = null
}

@BindingAdapter("app:setNicknameError")
fun setNicknameError(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError) textInputLayout.error = "중복되는 닉네임입니다."
    else textInputLayout.error = null
}

@BindingAdapter("app:setLoginPasswordError")
fun setLoginPasswordError(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError) textInputLayout.error = "비밀번호가 일치하지 않습니다."
    else textInputLayout.error = null
}