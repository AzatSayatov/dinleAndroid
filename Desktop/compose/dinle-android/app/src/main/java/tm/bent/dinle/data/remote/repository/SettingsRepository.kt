package tm.bent.dinle.data.remote.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tm.bent.dinle.ui.util.LocaleHelper.SELECTED_LANGUAGE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SharedPreferences.OnSharedPreferenceChangeListener {

    private val _locale = MutableStateFlow("tk")
    val locale = _locale.asStateFlow()

    fun produceLocaleValue(locale: String){
        _locale.value = locale
    }

    private fun getLocalePreferences(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.registerOnSharedPreferenceChangeListener(this)
        when(preferences.getString(SELECTED_LANGUAGE, "tk")){
            "ru" -> {
                produceLocaleValue("ru")
            }
            else -> {
                produceLocaleValue("tk")
            }
        }
    }

    init {
        getLocalePreferences()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        when(p0?.getString(SELECTED_LANGUAGE, "tk")){
            "ru" -> {
                produceLocaleValue("ru")
            }
            else -> {
                produceLocaleValue("tk")
            }
        }
    }

    companion object {
        private const val TAG = "Locale Repository"
    }
}