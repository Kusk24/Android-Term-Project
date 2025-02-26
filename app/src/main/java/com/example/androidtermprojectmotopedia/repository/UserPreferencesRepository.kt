package com.example.androidtermprojectmotopedia.repository
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")

    private val LANGUAGE = stringPreferencesKey("LANGUAGE")

    private val DARKTHEME = booleanPreferencesKey("DARKTHEME")

    private val NOTIPERMISSION = booleanPreferencesKey("NOTIPERMISSION")

    private val CURRENT_USER = stringPreferencesKey("CURRENT_USER")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "english"
    }

    val darkTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARKTHEME] ?: false
    }

    val notiPermission: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIPERMISSION] ?: false
    }

    val currentUser: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_USER] ?: ""
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean){
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun setLanguage(language: String){
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    suspend fun setDarkTheme(darkTheme: Boolean){
        context.dataStore.edit{ preferences ->
            preferences[DARKTHEME] = darkTheme
        }
    }

    suspend fun setNotiPermission(notiPermission: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIPERMISSION] = notiPermission
        }
    }

    suspend fun setCurrentUser(currentUser : String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER] = currentUser
        }
    }

}