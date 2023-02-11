package com.nekkiichi.storyapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class AppPreferences constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(tokenId: String) {
        Log.d(TAG,"session saved")
        dataStore.edit {
            it[TOKEN_KEY] = tokenId
        }
    }
    suspend fun clearSession() {
        dataStore.edit {
            it[TOKEN_KEY] = ""
        }
    }
    fun getToken(): Flow<String?> {
        return dataStore.data.map {
            it[TOKEN_KEY]
        }
    }
    suspend fun getTokenRaw(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }
    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token_key")
        val TAG = this::class.java.simpleName.toString()
    }
}