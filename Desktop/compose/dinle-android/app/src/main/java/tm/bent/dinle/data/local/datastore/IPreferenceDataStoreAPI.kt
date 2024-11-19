package tm.bent.dinle.data.local.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow


interface IPreferenceDataStoreAPI {
     fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<T>,defaultValue: T):T
    suspend fun <T> putPreference(key: Preferences.Key<T>,value:T)
    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun clearAllPreference()
}

object PreferenceDataStoreConstants {
    val NOTIFICATIONS_KEY = booleanPreferencesKey("NOTIFICATIONS")
    val USER_ID_KEY = stringPreferencesKey("USER_ID")
    val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
    val FIRST_TIME = booleanPreferencesKey("FIRST_TIME")
    val PHONE_KEY = stringPreferencesKey("PHONE")
    val LANGUAGE_KEY = stringPreferencesKey("LANGUAGE")
}