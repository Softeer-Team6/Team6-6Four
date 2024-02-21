package com.softeer.team6four.data.remote.fcm.source

import com.softeer.team6four.api.FcmService
import com.softeer.team6four.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FcmDataSource @Inject constructor(private val fcmService: FcmService) {
    fun postToken(accessToken: String, fcmToken: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val response = fcmService.postToken(authorization, fcmToken)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.code(), response.message()))
            }

        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}