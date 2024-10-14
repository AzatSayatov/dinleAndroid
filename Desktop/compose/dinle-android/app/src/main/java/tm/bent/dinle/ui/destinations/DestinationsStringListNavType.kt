package tm.bent.dinle.ui.destinations

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType

object DestinationsStringListNavType : NavType<List<String>>(false) {



    override fun get(bundle: Bundle, key: String): List<String>? {
        TODO("Not yet implemented")
    }

    override fun parseValue(value: String): List<String>{
        return value.split(",") // Разделяем строку на список
    }

    override fun put(bundle: Bundle, key: String, value: List<String>) {
        bundle.putStringArrayList(key, ArrayList(value)) // Сохраняем список как ArrayList
    }

    fun serializeValue(key: String, value: List<String>): String {
        return value.joinToString(",") // Преобразуем список в строку
    }
}
