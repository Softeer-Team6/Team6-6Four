package com.softeer.team6four.data.remote.charger.source

import com.softeer.team6four.api.ChargerService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
}