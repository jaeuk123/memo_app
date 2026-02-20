package com.test.memoapp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.test.memoapp.core.network.AuthResponse
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val USER_INFO = stringPreferencesKey("user_info")

    val GSON = Gson()

    suspend fun saveUserProfile(User: AuthResponse) {
        dataStore.edit { preferences ->
            preferences[USER_INFO] = GSON.toJson(User)
        }
    }

    val userFlow: Flow<AuthResponse?> = dataStore.data.map { preferences ->
        val json = preferences[USER_INFO]
        if (json == null) null
        else GSON.fromJson(json, AuthResponse::class.java)
    }

    suspend fun clearToken() {
        dataStore.edit { it.clear() }
    }
}