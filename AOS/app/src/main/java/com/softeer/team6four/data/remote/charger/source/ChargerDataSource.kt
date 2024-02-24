package com.softeer.team6four.data.remote.charger.source

import android.util.Log
import com.softeer.team6four.api.ChargerService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.charger.dto.ChargerRegistrationInfo
import com.softeer.team6four.data.remote.charger.dto.MyChargerDetailInfo
import com.softeer.team6four.data.remote.charger.dto.MyChargerList
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.data.remote.charger.model.ChargerDetailModel
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import com.softeer.team6four.data.remote.charger.model.RegistrationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChargerDataSource @Inject constructor(private val chargerService: ChargerService) {
    fun fetchMapChargerModelList(
        token: String,
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<List<MapChargerModel>>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $token"
            val response = chargerService.getMapChargerListDto(
                authorization,
                latitude,
                longitude,
            )
            if (response.isSuccessful) {
                val mapChargerListDto = response.body()?.data
                if (mapChargerListDto != null) {
                    emit(Resource.Success(mapChargerListDto.content.map { mapChargerInfo ->
                        MapChargerModel(
                            mapChargerInfo.carbobId,
                            mapChargerInfo.feePerHour,
                            mapChargerInfo.latitude,
                            mapChargerInfo.longitude
                        )
                    }))
                } else emit(Resource.Error(message = "body is null"))
            } else emit(Resource.Error(response.code(), "fail to get main charger list"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun fetchBottomSheetChargerModelList(
        token: String, latitude: Double,
        longitude: Double, sortType: String?
    ): Flow<Resource<List<BottomSheetChargerModel>>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $token"
            val response = chargerService.getBottomSheetChargerListDto(
                token = authorization,
                latitude = latitude,
                longitude = longitude,
                sortType = sortType
            )
            if (response.isSuccessful) {
                val bottomSheetChargerListDto = response.body()?.data
                if (bottomSheetChargerListDto != null) {
                    emit(Resource.Success(bottomSheetChargerListDto.content.map { bottomSheetChargerInfo ->
                        BottomSheetChargerModel(
                            address = bottomSheetChargerInfo.address,
                            chargerId = bottomSheetChargerInfo.carbobId,
                            feePerHour = bottomSheetChargerInfo.feePerHour,
                            nickname = bottomSheetChargerInfo.nickname,
                            speedType = bottomSheetChargerInfo.speedType
                        )
                    }))
                } else emit(Resource.Error(message = "body is null"))
            } else emit(Resource.Error(response.code(), "fail to get bottom sheet charger list"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun uploadImage(token: String, imageFile: File): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $token"
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = chargerService.uploadImage(authorization, body)
            if (response.isSuccessful) {
                val imageUrl = response.body()?.data
                imageUrl?.let { url ->
                    emit(Resource.Success(url.imgUrl))
                }
            } else {
                emit(Resource.Error(response.code(), "fail to upload image"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun fetchMyChargerList(
        accessToken: String, sortType: String, lastCarbobId: Int?, lastReservationCount: Int?
    ): Flow<Resource<MyChargerList>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val myChargerListResponse = chargerService.getMyChargerList(
                token = authorization,
                sortType = sortType,
                lastCarbobId = lastCarbobId,
                lastReservationCount = lastReservationCount
            )
            if (myChargerListResponse.isSuccessful) {
                val myChargerListDto = myChargerListResponse.body()?.data
                emit(
                    Resource.Success(
                        myChargerListDto ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(myChargerListResponse.code(), myChargerListResponse.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun registerCharger(token: String, registrationModel: RegistrationModel): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            try {
                val authorization = "Bearer $token"
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDateTime =
                    sdf.format(
                        Calendar.getInstance().time
                    ).plus("T${String.format("%02d", registrationModel.startTime)}:00:00.000000")
                val endDateTime =
                    sdf.format(
                        Calendar.getInstance().time
                    ).plus("T${String.format("%02d", registrationModel.endTime)}:00:00.000000")
                val response = chargerService.registerCharger(
                    authorization,
                    ChargerRegistrationInfo(
                        carbobNickname = registrationModel.chargerNickname,
                        address = registrationModel.address,
                        description = registrationModel.description,
                        feePer1kwh = registrationModel.feePer1kWh,
                        latitude = registrationModel.latitude,
                        longitude = registrationModel.longitude,
                        locationType = registrationModel.locationType,
                        speedType = registrationModel.speedType,
                        installType = registrationModel.installType,
                        chargeType = registrationModel.chargeType,
                        startDateTime = startDateTime,
                        endDateTime = endDateTime,
                        carbobImgUrl = registrationModel.imageUrl
                    )
                )
                if (response.isSuccessful) {
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(response.code(), response.errorBody()?.string()!!))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message.toString()))
            }
        }


    fun fetchChargerDetailInfo(
        accessToken: String, carbobId: Int
    ): Flow<Resource<MyChargerDetailInfo>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val chargerDetailResponse = chargerService.getMyChargerDetail(
                token = authorization,
                carbobId = carbobId,
            )
            if (chargerDetailResponse.isSuccessful) {
                val chargerDetailDto = chargerDetailResponse.body()?.data
                Log.i("chargerDetailDto", "fetchChargerDetailInfo: $chargerDetailDto")
                emit(
                    Resource.Success(
                        chargerDetailDto ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(chargerDetailResponse.code(), chargerDetailResponse.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
    fun fetchChargerDetail(
        token: String,
        chargerId: Long,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<ChargerDetailModel>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $token"
            val response =
                chargerService.getChargerDetail(authorization, chargerId, latitude, longitude)
            if (response.isSuccessful) {
                val chargerDetailInfo = response.body()?.data
                if (chargerDetailInfo != null) {
                    emit(
                        Resource.Success(
                            ChargerDetailModel(
                                address = chargerDetailInfo.address,
                                chargerId = chargerDetailInfo.carbobId,
                                chargerType = chargerDetailInfo.chargerType,
                                description = chargerDetailInfo.description,
                                distance = chargerDetailInfo.distance,
                                feePerHour = chargerDetailInfo.feePerHour,
                                imageUrl = chargerDetailInfo.imageUrl,
                                installType = chargerDetailInfo.installType,
                                nickname = chargerDetailInfo.nickname,
                                speedType = chargerDetailInfo.speedType
                            )
                        )
                    )
                } else emit(Resource.Error(response.code(), "fail to get charger detail"))
            } else {
                emit(Resource.Error(message = "body is null"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}

