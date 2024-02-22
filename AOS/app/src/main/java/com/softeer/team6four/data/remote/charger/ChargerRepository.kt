package com.softeer.team6four.data.remote.charger

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.data.remote.charger.model.ChargerDetailModel
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import com.softeer.team6four.data.remote.charger.source.ChargerDataSource
import kotlinx.coroutines.flow.Flow
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

    fun fetchChargerDetail(
        token: String,
        chargerId: Long,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<ChargerDetailModel>> {
        return chargerDataSource.fetchChargerDetail(token, chargerId, latitude, longitude)
    }
}