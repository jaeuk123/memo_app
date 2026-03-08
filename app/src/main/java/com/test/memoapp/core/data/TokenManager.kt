package com.test.memoapp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
//import com.google.gson.Gson
import com.test.memoapp.core.network.AuthResponse
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val USER_INFO = stringPreferencesKey("user_info")

//    val GSON = Gson()

    suspend fun saveUserProfile(user: AuthResponse) {
        dataStore.edit { preferences ->
            val encodeToString = Json.encodeToString(user)
            preferences[USER_INFO] = encodeToString
            println("userFlow.first() = ${userFlow.first()}")

        }
    }

    val userFlow: Flow<AuthResponse?> = dataStore.data.map { preferences ->
        val json = preferences[USER_INFO]
        if (json == null) null
        else Json.decodeFromString<AuthResponse>(json)
    }

    val isLoggedIn: Flow<Boolean> = userFlow.map { it != null }

    suspend fun clearToken() {
        dataStore.edit { it.clear() }
    }
}