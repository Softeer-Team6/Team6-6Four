package com.softeer.team6four.ui.mypage.charger

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyChargerViewModel @Inject constructor(): ViewModel() {
    private val _chargerId: MutableStateFlow<Int> = MutableStateFlow(0)
    val chargerId: StateFlow<Int> = _chargerId

    private val _chargerName: MutableStateFlow<String> = MutableStateFlow("")
    val chargerName: StateFlow<String> = _chargerName

    fun updateChargerNickname(chargerName: String) {
        _chargerName.value = chargerName
    }

    fun updateChargerId(chargerId: Int) {
        _chargerId.value = chargerId
    }
}