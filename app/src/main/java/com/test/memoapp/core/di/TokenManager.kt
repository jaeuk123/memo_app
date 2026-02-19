package com.test.memoapp.core.di

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.test.memoapp.core.network.AuthResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException


@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }
}

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "auth_prefs")
    private val USER_INFO = stringPreferencesKey("user_info")

    val GSON = Gson()

    suspend fun saveUserProfile(User : AuthResponse) {
        context.dataStore.edit { preferences ->
            preferences[USER_INFO] = GSON.toJson(User)
        }
    }

    val userFlow: Flow<AuthResponse?> = context.dataStore.data.map{ preferences ->
        val json = preferences[USER_INFO]
        if (json == null) null
        else GSON.fromJson(json, AuthResponse::class.java)
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.clear() }
    }
}