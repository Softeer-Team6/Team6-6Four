package com.softeer.team6four.data

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.user.dto.SignUpInfo
import com.softeer.team6four.data.remote.user.model.EmailCheckModel
import com.softeer.team6four.data.remote.user.model.NicknameCheckModel
import com.softeer.team6four.data.remote.user.model.UserInfoModel
import com.softeer.team6four.data.remote.user.model.UserLoginModel
import com.softeer.team6four.data.remote.user.source.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDataSource: UserDataSource) {
    fun fetchEmailCheck(email: String): Flow<Resource<EmailCheckModel>> {
        return userDataSource.checkEmail(email).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val emailExistsResult = resource.data
                    val emailCheckModel = EmailCheckModel(emailExistsResult.emailExists)
                    Resource.Success(emailCheckModel)
                }

                is Resource.Error -> {
                    Resource.Error(message = resource.message)
                }

                else -> {
                    Resource.Loading()
                }
            }
        }
    }

    fun fetchNicknameCheck(nickname: String): Flow<Resource<NicknameCheckModel>> {
        return userDataSource.checkNickname(nickname).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val nicknameExistsResult = resource.data
                    val emailCheckModel = NicknameCheckModel(nicknameExistsResult.nicknameExists)
                    Resource.Success(emailCheckModel)
                }

                is Resource.Error -> {
                    Resource.Error(message = resource.message)
                }

                else -> {
                    Resource.Loading()
                }
            }
        }
    }

    fun requestSignUp(userInfoModel: UserInfoModel): Flow<Resource<Any>> {
        val singUpInfo = SignUpInfo(
            email = userInfoModel.email,
            password = userInfoModel.password,
            nickname = userInfoModel.nickname
        )
        return userDataSource.requestSignUp(singUpInfo)
    }

    fun requestLogin(nickname: String, password: String): Flow<Resource<UserLoginModel>> {
        return userDataSource.requestLogin(nickname, password).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val result = resource.data
                    val userLoginModel = UserLoginModel(
                        nickname = result.nickname,
                        accessToken = result.accessToken,
                        refreshToken = result.refreshToken
                    )
                    Resource.Success(userLoginModel)
                }

                is Resource.Error -> {
                    Resource.Error(code = resource.code, message = resource.message)
                }

                else -> {
                    Resource.Loading()
                }
            }
        }
    }

}