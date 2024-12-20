package tm.bent.dinle.di



import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.cronet.CronetUtil
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import tm.bent.dinle.player.DOWNLOAD_CONTENT_DIRECTORY
import tm.bent.dinle.player.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.MyPlayer
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.player.getDownloadDirectory
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AudioPlayer


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VideoPlayer

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }


    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDatabaseProvider(context: Context): DatabaseProvider {
        return StandaloneDatabaseProvider(context);

    }

    @Provides
    fun provideDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil =
        DataStoreUtil(context)

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDownloadCache(
        context: Context,
        databaseProvider: DatabaseProvider
    ): Cache {
        val downloadContentDirectory = File(
            getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY
        )
        return SimpleCache(
            downloadContentDirectory,
            NoOpCacheEvictor(),
            databaseProvider
        )
    }


    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDownloadManager(
        context: Context,
        databaseProvider: DatabaseProvider,
        cache: Cache,
        dataSourceFactory: DataSource.Factory
    ): DownloadManager {
        return DownloadManager(
            context,
            databaseProvider,
            cache,
            dataSourceFactory,
            Executors.newFixedThreadPool( /* nThreads= */6)
        )
    }


    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDownloadNotificationHelper(
        context: Context
    ): DownloadNotificationHelper {
        return DownloadNotificationHelper(
            context, DOWNLOAD_NOTIFICATION_CHANNEL_ID
        )
    }


    @Synchronized
    fun getHttpDataSourceFactory(context: Context): DataSource.Factory {

        val cronetEngine = CronetUtil.buildCronetEngine(context)
        if (cronetEngine != null) {
            return CronetDataSource.Factory(
                cronetEngine,
                Executors.newSingleThreadExecutor()
            )
        } else {

            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(cookieManager)
            return DefaultHttpDataSource.Factory()
        }

    }


    @OptIn(UnstableApi::class)
    @Synchronized
    @Provides
    @Singleton
    fun getDownloadTracker(context: Context, downloadManager: DownloadManager): DownloadTracker {
        return DownloadTracker(
            context, getHttpDataSourceFactory(context), downloadManager
        )
    }


    @OptIn(UnstableApi::class)
    @Synchronized
    @Provides
    @Singleton
    fun provideDataSourceFactory(
        context: Context,
        cache: Cache
    ): DataSource.Factory {

        val upstreamFactory = DefaultDataSource.Factory(
            context,
            getHttpDataSourceFactory(context)
        )
        val dataSourceFactory =
            buildReadOnlyCacheDataSource(
                upstreamFactory,
                cache
            )

        return dataSourceFactory
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun buildReadOnlyCacheDataSource(
        upstreamFactory: DataSource.Factory,
        cache: Cache
    ): CacheDataSource.Factory {
        val cacheSink = CacheDataSink.Factory()
            .setCache(cache)
        val downStreamFactory = FileDataSource.Factory()

        return CacheDataSource.Factory().setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setCacheReadDataSourceFactory(downStreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }


    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    @AudioPlayer
    fun provideExoPLayer(
        context: Context,
        httpDataSourceFactory: CacheDataSource.Factory
    ): ExoPlayer {

        val hlsMediaSource =
            HlsMediaSource.Factory(httpDataSourceFactory)

        val player = ExoPlayer.Builder(context).setMediaSourceFactory(hlsMediaSource).build()

        return player
    }


    @Provides
    @Singleton
    fun provideMediaSession(
        context: Context,
        @AudioPlayer player: ExoPlayer
    ): MediaSession {
        val sessionActivityPendingIntent =
            context.packageManager?.getLaunchIntentForPackage(context.packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(context, 0, sessionIntent, PendingIntent.FLAG_MUTABLE)
            }

        return MediaSession.Builder(context, player)
            .setSessionActivity(sessionActivityPendingIntent!!).build()

    }

//
//    @Provides
//    @Singleton
//    fun provideMediaNotificationManager(
//        context: Context,
//        player: ExoPlayer,
//        mediaSession: MediaSession
//    ): MediaNotificationManager {
//        return MediaNotificationManager(
//            context,
//            mediaSession.token,
//            player,
//        )
//    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    @VideoPlayer
    fun provideVideoPLayer(
        context: Context,
        httpDataSourceFactory: CacheDataSource.Factory
    ): ExoPlayer {

        val hlsMediaSource =
            HlsMediaSource.Factory(httpDataSourceFactory)

        val player = ExoPlayer.Builder(context).setMediaSourceFactory(hlsMediaSource).build()

        return player
    }
    @Provides
    @Singleton
    fun provideMyPlayer(@AudioPlayer player: ExoPlayer): MyPlayer {
        return MyPlayer(player)
    }


    @Provides
    @Singleton
    fun provideMainController(player: MyPlayer, ): PlayerController {
        return PlayerController(player)
    }


}