package tm.bent.dinle.ui.destinations.downloads

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import java.io.File
import javax.inject.Inject


@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
) : ViewModel() {

    private val _songs = MutableStateFlow<MutableList<Song>>(mutableListOf())
    val songs: StateFlow<MutableList<Song>> = _songs

    fun getDownloadedSongs(): Flow<MutableList<Song>> {
        return songRepository.getSongs()
    }

    init {
        viewModelScope.launch {
            songRepository.getSongs().collect { songList ->
                _songs.value = songList.toMutableList()
            }
        }
    }


    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker



    fun likeSong(id:String){
        viewModelScope.launch {
            try {
                songRepository.likeSong(id)
            }catch (e:Exception){
                Log.e("TAG", "likeSong: "+e.message )
            }
        }
    }

    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                songRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                songRepository.insertSong(song = song)

            }
        }
    }

    fun removeSongAt(index: Int) {
        val updatedList = _songs.value.toMutableList()
        if (index in updatedList.indices) {
            val song = updatedList[index]
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    // Удаляем из базы данных
                    songRepository.deleteSongById(song.id)

                    // Удаляем файл, если он существует
                    val fileUri = Uri.parse(song.link)
                    val file = File(fileUri.path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                updatedList.removeAt(index)
                _songs.value = updatedList
            }
        }
    }
}
