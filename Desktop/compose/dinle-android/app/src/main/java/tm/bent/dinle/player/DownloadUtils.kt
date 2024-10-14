
package tm.bent.dinle.player

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.DefaultRenderersFactory.ExtensionRendererMode
import androidx.media3.exoplayer.RenderersFactory
import java.io.File

val USE_CRONET_FOR_NETWORKING = true


val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"

fun useExtensionRenderers(): Boolean {
    return true
}



@Synchronized
fun getDownloadDirectory(context: Context): File? {

    var downloadDirectory = context.getExternalFilesDir( /* type= */null)

    if (downloadDirectory == null) {
        downloadDirectory = context.filesDir
    }

    return downloadDirectory
}

@UnstableApi
fun buildRenderersFactory(
    context: Context, preferExtensionRenderer: Boolean
): RenderersFactory {
    val extensionRendererMode: @ExtensionRendererMode Int =
        if (useExtensionRenderers()) (
                if (preferExtensionRenderer)
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
    return DefaultRenderersFactory(context).setExtensionRendererMode(extensionRendererMode)
}








