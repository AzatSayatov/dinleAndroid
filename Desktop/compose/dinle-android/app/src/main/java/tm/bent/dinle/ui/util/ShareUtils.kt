package tm.bent.dinle.ui.util

import android.content.Context
import android.content.Intent

object ShareUtils {

    fun shareLink(context: Context, link: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }


}