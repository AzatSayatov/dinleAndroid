package tm.bent.dinle


//@Singleton
//class LocaleController @Inject constructor(
//    @ApplicationContext private val context: Context,
//) : SharedPreferences.OnSharedPreferenceChangeListener {
//
//    private val _locale = MutableStateFlow("tk")
//    val locale = _locale.asStateFlow()
//
//    fun produceLocaleValue(locale: String){
//        _locale.value = locale
//    }
//
//    private fun getLocalePreferences(){
//        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//        preferences.registerOnSharedPreferenceChangeListener(this)
//        when(preferences.getString(SELECTED_LANGUAGE, "tk")){
//            "ru" -> {
//                produceLocaleValue("ru")
//            }
//            "tk" -> {
//                produceLocaleValue("tk")
//            }
//        }
//    }
//
//    init {
//        getLocalePreferences()
//    }
//
//    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
//        when(p0?.getString(SELECTED_LANGUAGE, "tk")){
//            "ru" -> {
//                produceLocaleValue("ru")
//            }
//            "tk" -> {
//                produceLocaleValue("tk")
//            }
//        }
//    }
//
//    companion object {
//        private const val TAG = "Locale Repository"
//    }
//}

sealed class SupportedLanguage(
    val id: Int,
    val name: String,
    val key: String
) {
    object Turkmen : SupportedLanguage(1, "Türkmen", key = "tk")
    object Russian : SupportedLanguage(2, "Русский", key = "ru")
//    object English : SupportedLanguage(3, "English", key = "en")
}

val supportedLanguages = listOf(
    SupportedLanguage.Turkmen,
    SupportedLanguage.Russian,
//    SupportedLanguage.English,
)