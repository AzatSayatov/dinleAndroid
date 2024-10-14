package tm.bent.dinle.ui.destinations.artists

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import tm.bent.dinle.domain.model.Artist

class ArtistsPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("artist_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Сохранение списка артистов
    fun saveArtists(artists: List<Artist>) {
        val json = gson.toJson(artists)
        sharedPreferences.edit().putString("subscribed_artists", json).apply()
    }

    // Получение списка артистов
    fun getSubscribedArtists(): List<Artist> {
        val json = sharedPreferences.getString("subscribed_artists", null)
        return if (json != null) {
            val type = object : TypeToken<List<Artist>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Проверка подписки на артиста
    fun isSubscribed(artistId: String): Boolean {
        return getSubscribedArtists().any { it.id == artistId }
    }

    // Добавление артиста в подписку
    fun addArtist(artist: Artist) {
        val artists = getSubscribedArtists().toMutableList()
        artists.add(artist)
        saveArtists(artists)
    }

    // Удаление артиста из подписки
    fun removeArtist(artistId: String) {
        val artists = getSubscribedArtists().filter { it.id != artistId }
        saveArtists(artists)
    }
}


