package com.softeer.team6four.data

import com.naver.maps.geometry.LatLng
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.api.GeoCodeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeoCodeRepository(private val geoCodeService: GeoCodeService) {
    suspend fun getCoordinateResult(address: String): Flow<LatLng> {
        val result = geoCodeService.coordinateResult(
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            address = address,
            count = 1
        )
        return flow {
            if (result.isSuccessful) {
                result.body()?.let { geoCodeResult ->
                    geoCodeResult.addresses?.let { addresses ->
                        if (addresses.isNotEmpty()) {
                            emit(
                                LatLng(
                                    addresses[0].y.toDouble(),
                                    addresses[0].x.toDouble()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}