package tm.bent.dinle.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tm.bent.dinle.data.remote.interceptor.LanguageInterceptor
import tm.bent.dinle.data.remote.interceptor.SynchronizedAuthenticator
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.data.remote.service.TokenRefreshService
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


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
        languageInterceptor: LanguageInterceptor
    ): OkHttpClient {

//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(SynchronizedAuthenticator)
//            .addInterceptor(interceptor)
            .addInterceptor(languageInterceptor)
            .build()
    }

    @OtherInterceptorOkHttpClient
    @Provides
    fun provideOkHttpClient(languageInterceptor: LanguageInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
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