package com.example.simplyachivs.data.repositoryImpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.simplyachivs.domain.repository.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionRepository {

    private val userIdKey = stringPreferencesKey("user_id")
    private val lastResetDateKey = stringPreferencesKey("last_reset_date")

    override val userId: Flow<UUID?> = context.dataStore.data.map { prefs ->
        prefs[userIdKey]?.let { UUID.fromString(it) }
    }

    override val lastResetDate: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[lastResetDateKey]
    }

    override suspend fun saveUserId(userId: UUID) {
        context.dataStore.edit { prefs ->
            prefs[userIdKey] = userId.toString()
        }
    }

    override suspend fun saveLastResetDate(date: String) {
        context.dataStore.edit { prefs ->
            prefs[lastResetDateKey] = date
        }
    }

    override suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(userIdKey)
        }
    }
}

