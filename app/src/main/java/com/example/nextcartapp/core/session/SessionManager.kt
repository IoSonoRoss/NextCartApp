package com.example.nextcartapp.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[ACCESS_TOKEN_KEY] }

    val userName: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    suspend fun saveSession(token: String, userId: String, name: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[USER_NAME_KEY] = name
            preferences[USER_EMAIL_KEY] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Metodo per verificare se l'utente è loggato
    suspend fun isTokenValid(): Boolean {
        val token = context.dataStore.data.first()[ACCESS_TOKEN_KEY]  // ← CONTEXT.dataStore
        return !token.isNullOrEmpty()
    }

    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->  // ← CONTEXT.dataStore
        !preferences[ACCESS_TOKEN_KEY].isNullOrEmpty()
    }
}