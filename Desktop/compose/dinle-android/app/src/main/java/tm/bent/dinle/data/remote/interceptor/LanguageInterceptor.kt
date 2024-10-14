package tm.bent.dinle.data.remote.interceptor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.ui.util.LocaleHelper
import javax.inject.Inject


class LanguageInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val language = LocaleHelper.getLanguage(context)
        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", language)
            .build()
        return chain.proceed(requestBuilder)
    }

}