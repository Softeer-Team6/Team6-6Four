package com.softeer.team6four.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val fcmTokenKey = stringPreferencesKey("fcm_token")
    private val nicknameKey = stringPreferencesKey("nickname")

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[accessTokenKey] ?: ""
        }
    }

    fun getRefreshToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[refreshTokenKey] ?: ""
        }
    }

    fun getNickname(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[nicknameKey] ?: ""
        }
    }

    fun getFcmToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[fcmTokenKey] ?: ""
        }
    }

    suspend fun updateAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[refreshTokenKey] = refreshToken
        }
    }

    suspend fun updateFcmToken(fcmToken: String) {
        dataStore.edit { preferences ->
            preferences[fcmTokenKey] = fcmToken
        }
    }

    suspend fun updateNickname(nickname: String) {
        dataStore.edit { preferences ->
            preferences[nicknameKey] = nickname
        }
    }

    suspend fun allClear() {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = ""
            preferences[refreshTokenKey] = ""
            preferences[nicknameKey] = ""
        }
    }
}