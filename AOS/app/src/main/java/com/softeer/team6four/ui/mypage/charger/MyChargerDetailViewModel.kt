package com.softeer.team6four.ui.mypage.charger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.UserPreferencesRepository
import com.softeer.team6four.data.ChargerRepository
import com.softeer.team6four.data.remote.charger.model.MyChargerDetailInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChargerDetailViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val chargerRepository: ChargerRepository
) : ViewModel(){
    private val _chargerId: MutableStateFlow<Int> = MutableStateFlow(0)
    val chargerId: MutableStateFlow<Int> = _chargerId

    private val _myChargerDetail: MutableStateFlow<MyChargerDetailInfoModel> = MutableStateFlow(MyChargerDetailInfoModel())
    val myChargerDetail: MutableStateFlow<MyChargerDetailInfoModel> = _myChargerDetail

    fun updateChargerId(chargerId: Int) {
        _chargerId.value = chargerId
    }

    fun fetchMyChargerDetail(chargerId: Int) {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myChargerDetailData = chargerRepository.fetchChargerDetailInfo(
                accessToken = accessToken,
                chargerId = chargerId
            )

            myChargerDetailData.catch {
                Log.e("fetchChargerDetailInfo", "fetchMyChargerDetail: $it")
            }.collect {resource ->
                when (resource) {
                    is Resource.Success -> {
                        _myChargerDetail.value = resource.data
                    }

                    is Resource.Error -> {
                        Log.e("fetchChargerDetailInfo", "fetchMyChargerDetail: ${resource.message}")
                    }

                    else -> {
                        Log.e("fetchChargerDetailInfo", "fetchMyChargerDetail: loading")
                    }
                }
            }
        }
    }
}