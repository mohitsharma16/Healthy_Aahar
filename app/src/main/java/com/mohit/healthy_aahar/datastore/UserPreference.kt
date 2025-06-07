package com.mohit.healthy_aahar.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// Create an extension property for DataStore
val Context.dataStore by preferencesDataStore("user_prefs")



object UserPreference {

    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    // Check if user has seen onboarding (synchronous)
    fun hasSeenOnboarding(context: Context): Boolean {
        return runBlocking {
            try {
                val preferences = context.dataStore.data.first()
                preferences[ONBOARDING_COMPLETED] ?: false
            } catch (e: Exception) {
                false
            }
        }
    }

    // Mark onboarding as completed
    suspend fun setOnboardingCompleted(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }

    // Flow version for reactive programming
    fun hasSeenOnboardingFlow(context: Context) = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    private val UID_KEY = stringPreferencesKey("user_uid")

    fun getUidFlow(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[UID_KEY]
        }
    }

    suspend fun saveUid(context: Context, uid: String) {
        context.dataStore.edit { prefs ->
            prefs[UID_KEY] = uid
        }
    }

    suspend fun clearUid(context: Context) {
        context.dataStore.edit {
            it.remove(UID_KEY)
        }
    }

    // Clear all user data (for logout functionality)
    suspend fun clearUserData(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear() // This will clear all stored preferences
        }
    }
}