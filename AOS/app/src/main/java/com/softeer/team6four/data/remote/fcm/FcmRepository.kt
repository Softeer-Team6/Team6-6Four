package com.softeer.team6four.data.remote.fcm

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.fcm.source.FcmDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FcmRepository @Inject constructor(private val fcmDataSource: FcmDataSource) {
    fun postToken(accessToken: String, fcmToken: String) : Flow<Resource<Unit>> {
        return fcmDataSource.postToken(accessToken, fcmToken)
    }
}