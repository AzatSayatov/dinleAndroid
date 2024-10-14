package tm.bent.dinle.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.Song


@Database(entities = [Song::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun songDao(): SongDao
}