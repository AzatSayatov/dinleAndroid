package tm.bent.dinle

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import dagger.hilt.android.HiltAndroidApp
import tm.bent.dinle.player.PlaybackService
import tm.bent.dinle.ui.util.LocaleHelper


@HiltAndroidApp
class DinleApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val factory = MediaController.Builder(
            applicationContext,
            SessionToken(applicationContext, ComponentName(applicationContext, PlaybackService::class.java))
        ).buildAsync()
    }

    override fun attachBaseContext(base: Context?) {
        val config = Resources.getSystem().configuration
        val locale = config.locales.get(0)
        base?.let {
            if(locale.language == "ru"){
                super.attachBaseContext(LocaleHelper.onAttach(base,"ru" ))
            } else {
                super.attachBaseContext(LocaleHelper.onAttach(base,"tk" ))
            }
        }

    }
}