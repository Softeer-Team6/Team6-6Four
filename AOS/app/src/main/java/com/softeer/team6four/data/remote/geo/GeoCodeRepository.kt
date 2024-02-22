package com.softeer.team6four.data.remote.geo

import com.naver.maps.geometry.LatLng
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.api.GeoCodeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoCodeRepository @Inject constructor(private val geoCodeService: GeoCodeService) {
    suspend fun getCoordinateResult(address: String): Flow<Result<LatLng>> {
        val result = geoCodeService.coordinateResult(
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            address = address,
            count = 1
        )
        return flow {
            if (result.isSuccessful) {
                result.body()?.addresses?.firstOrNull()?.let {address ->
                    emit(Result.success(LatLng(address.y.toDouble(), address.x.toDouble())))
                } ?: emit(Result.failure(Exception("No address found")))
            }
            else {
                emit(Result.failure(Exception("API Call unsuccessful with response code : ${result.code()}")))
            }
        }
    }
}