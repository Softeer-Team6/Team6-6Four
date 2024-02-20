package com.softeer.team6four.data.remote.user.source

import android.util.Log
import com.softeer.team6four.api.UserService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.user.dto.EmailExistsResult
import com.softeer.team6four.data.remote.user.dto.NicknameExistsResult
import com.softeer.team6four.data.remote.user.dto.SignUpInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(private val userService: UserService) {

    fun checkEmail(email: String): Flow<Resource<EmailExistsResult>> = flow {
        emit(Resource.Loading())
        try {
            val response = userService.checkEmail(email)
            if (response.isSuccessful) {
                val emailExistsResult = response.body()?.data
                emit(
                    Resource.Success(
                        emailExistsResult ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(response.code(), "Failed to check email existence"))
            }
        } catch (e: Exception) {
            Log.e("emailCheckError", e.message.toString())
            emit(Resource.Error(message = "Failed to check email existence"))
        }
    }

    fun checkNickname(nickname: String): Flow<Resource<NicknameExistsResult>> = flow {
        emit(Resource.Loading())
        try {
            val response = userService.checkNickname(nickname)
            if(response.isSuccessful) {
                val nicknameExistsResult = response.body()?.data
                emit(
                    Resource.Success(
                        nicknameExistsResult ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(response.code(), "Failed to check nickname existence"))
            }

        } catch (e: Exception) {
            Log.e("nicknameCheckedError", e.message.toString())
            emit(Resource.Error(message = "Failed to check nickname existence"))
        }
    }

    fun requestSignUp(signUpInfo: SignUpInfo) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try{
            val signupResponse = userService.requestSignUp(signUpInfo)
            if(signupResponse.isSuccessful) {
                val signupResult = signupResponse.body()
                emit(
                    Resource.Success(
                        signupResult ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(signupResponse.code(), "Failed to Sign Up"))
            }
        } catch(e : Exception){
            Log.e("SignUpError", e.message.toString())
            emit(Resource.Error(message = "Failed to Sign Up"))
        }
    }
}