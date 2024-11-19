package tm.bent.dinle.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tm.bent.dinle.data.remote.interceptor.LanguageInterceptor
import tm.bent.dinle.data.remote.interceptor.SynchronizedAuthenticator
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.data.remote.service.TokenRefreshService
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OtherInterceptorOkHttpClient

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideOkHttpClientWithAuth(
        SynchronizedAuthenticator: SynchronizedAuthenticator,
        languageInterceptor: LanguageInterceptor,
    ): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)



        val trustAllCertificates: TrustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate>? = emptyArray()
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustAllCertificates), SecureRandom())

        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
//            .certificatePinner()
//            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates as X509TrustManager)
//            .hostnameVerifier{_,_ -> true}
            .addInterceptor(SynchronizedAuthenticator)
            .addInterceptor(languageInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @OtherInterceptorOkHttpClient
    @Provides
    fun provideOkHttpClient(languageInterceptor: LanguageInterceptor): OkHttpClient {

        val trustAllCertificates: TrustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustAllCertificates), SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient
            .Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
//            .sslSocketFactory(sslSocketFactory, trustAllCertificates as X509TrustManager)
//            .hostnameVerifier{_,_ -> true}
            .addInterceptor(languageInterceptor)
            .build()
    }

    @Provides
    fun buildTokenRefreshApi(
        @OtherInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): TokenRefreshService {
        val DATE_FORMAT = "yyyy-MM-dd' 'HH:mm:ss"

        val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TokenRefreshService::class.java)
    }

    @Provides
    fun provideRetrofitServiceBuilder(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): Retrofit {

        //date format for getting Date object from json
        val DATE_FORMAT = "yyyy-MM-dd' 'HH:mm:ss"

        val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}
const val BASE_URL = "https://dinle.com.tm/api/"
const val SHARE_PLAYLIST_URL = "https://dinle.com.tm/playlist/"
const val SHARE_ALBUM_URL = "https://dinle.com.tm/albom/"
const val SHARE_CLIP_URL = "https://dinle.com.tm/clips/"
const val SHARE_SONG_URL = "https://dinle.com.tm/"
const val SHARE_NEWS_URL = "https://dinle.com.tm/"
const val SHARE_ARTIST_URL = "https://dinle.com.tm/artist/"
//const val BASE_URL = "http://216.250.8.28:4033/api/"