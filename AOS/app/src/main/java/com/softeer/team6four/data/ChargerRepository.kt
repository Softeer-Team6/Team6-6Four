package com.softeer.team6four.data

import android.util.Log
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.data.remote.charger.model.ChargerDetailModel
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import com.softeer.team6four.data.remote.charger.model.MyChargerDetailInfoModel
import com.softeer.team6four.data.remote.charger.model.MyChargerListModel
import com.softeer.team6four.data.remote.charger.model.MyChargerSimpleInfoModel
import com.softeer.team6four.data.remote.charger.model.RegistrationModel
import com.softeer.team6four.data.remote.charger.source.ChargerDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChargerRepository @Inject constructor(private val chargerDataSource: ChargerDataSource) {
    fun fetchBottomSheetChargerList(
        token: String,
        latitude: Double,
        longitude: Double,
        sortType: String? = null
    ): Flow<Resource<List<BottomSheetChargerModel>>> {
        return chargerDataSource.fetchBottomSheetChargerModelList(
            token,
            latitude,
            longitude,
            sortType
        )
    }

    fun fetchMapChargerModelList(
        token: String,
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<List<MapChargerModel>>> {
        return chargerDataSource.fetchMapChargerModelList(token, latitude, longitude)
    }

    fun uploadImage(
        token: String,
        imageUrl: String
    ): Flow<Resource<String>> {
        if (imageUrl == "") {
            return chargerDataSource.uploadImage(token, null)
        }
        val imageFile = File(imageUrl)
        if (imageFile.isFile) Log.d("imagefile", "isFile")
        return chargerDataSource.uploadImage(token, imageFile)
    }

    fun registerCharger(
        token: String,
        chargerRegistrationModel: RegistrationModel
    ): Flow<Resource<Unit>> {
        return chargerDataSource.registerCharger(token, chargerRegistrationModel)
    }

    fun fetchMyChargerList(
        accessToken: String,
        sortType: String,
        lastChargerId: Int?,
        lastReservationId: Int?
    ): Flow<Resource<MyChargerListModel>> {
        return chargerDataSource.fetchMyChargerList(
            accessToken,
            sortType,
            lastChargerId,
            lastReservationId
        ).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val myChargerList = resource.data
                    val myChargerListModel = MyChargerListModel(
                        myChargerList.content.map {
                            MyChargerSimpleInfoModel(
                                carbobId = it.carbobId,
                                address = it.address,
                                imageUrl = it.imageUrl,
                                nickname = it.nickname,
                                reservationCount = it.reservationCount,
                            )
                        },
                        myChargerList.hasNext,
                        myChargerList.size
                    )
                    Resource.Success(myChargerListModel)
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

    fun fetchChargerDetailInfo(
        accessToken: String,
        chargerId: Int
    ): Flow<Resource<MyChargerDetailInfoModel>> {
        return chargerDataSource.fetchChargerDetailInfo(accessToken, chargerId).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val myChargerDetail = resource.data
                    val myChargerDetailInfo = MyChargerDetailInfoModel(
                        address = myChargerDetail.address,
                        carbobId = myChargerDetail.carbobId,
                        carbobTotalIncome = myChargerDetail.carbobTotalIncome,
                        chargerType = myChargerDetail.chargerType,
                        description = myChargerDetail.description,
                        feePerHour = myChargerDetail.feePerHour,
                        imageUrl = myChargerDetail.imageUrl,
                        qrImageUrl = myChargerDetail.qrImageUrl,
                        installType = myChargerDetail.installType,
                        nickname = myChargerDetail.nickname,
                        selfUseTime = myChargerDetail.selfUseTime,
                        speedType = myChargerDetail.speedType
                    )
                    Resource.Success(myChargerDetailInfo)
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

    fun fetchChargerDetail(
        token: String,
        chargerId: Long,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<ChargerDetailModel>> {
        return chargerDataSource.fetchChargerDetail(token, chargerId, latitude, longitude)
    }
}

