package com.nekkiichi.storyapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(userId: String, name: String, tokenId: String) {
        Log.d(TAG,"session saved")
        dataStore.edit {
            it[TOKEN_KEY] = tokenId
            it[USER_NAME] = name
            it[USER_ID] = userId
        }
    }
    suspend fun clearLoginStatus() {
        dataStore.edit {
            it[TOKEN_KEY] = ""
            it[USER_NAME] = ""
            it[USER_ID] = ""
        }
    }
    fun getToken(): Flow<String?> {
        return dataStore.data.map {
            it[TOKEN_KEY]
        }
    }
    fun getUserId(): Flow<String?> {
        return dataStore.data.map {
            it[USER_ID]
        }
    }
    fun getUserName(): Flow<String?> {
        return dataStore.data.map {
            it[USER_NAME]
        }
    }
    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token_key")
        val USER_NAME = stringPreferencesKey("auth_user_name")
        val USER_ID = stringPreferencesKey("auth_user_id")
        val TAG = this::class.java.simpleName.toString()
    }
}