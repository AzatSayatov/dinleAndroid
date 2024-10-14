package tm.bent.dinle.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tm.bent.dinle.domain.model.Song


@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: Song)

    @Transaction
    @Query("SELECT * FROM Song")
    fun getSongs(): Flow<MutableList<Song>>

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM song WHERE id = :songId")
    suspend fun deleteSongById(songId: String)

    @Update
    suspend fun updateSongsOrder(songs: List<Song>)


}