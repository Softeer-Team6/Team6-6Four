package com.softeer.team6four.data.remote.charger

import android.util.Log
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import com.softeer.team6four.data.remote.charger.model.RegistrationModel
import com.softeer.team6four.data.remote.charger.source.ChargerDataSource
import kotlinx.coroutines.flow.Flow
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
}