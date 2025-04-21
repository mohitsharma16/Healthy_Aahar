package com.mohit.healthy_aahar.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object FeedbackPreferences {
    private val Context.dataStore by preferencesDataStore(name = "feedback_prefs")

    val LAST_FEEDBACK_DATE = stringPreferencesKey("last_feedback_date")

    suspend fun saveFeedbackDate(context: Context, date: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_FEEDBACK_DATE] = date
        }
    }

    suspend fun getLastFeedbackDate(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[LAST_FEEDBACK_DATE]
    }
}
