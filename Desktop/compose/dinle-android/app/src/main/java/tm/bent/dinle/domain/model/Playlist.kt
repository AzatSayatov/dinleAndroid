package tm.bent.dinle.domain.model

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.concurrent.Immutable

@Immutable
data class Playlist(
    val id : String,
    val title : String,
    val cover : String,
    val description : String? = "",
    val songs : Long? = 0L,
    val isLiked : Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(cover)
        parcel.writeString(description)
        parcel.writeValue(songs)
        parcel.writeByte(if (isLiked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }

    fun getImage(): String {
        return cover
    }
}

val mockPlaylist = Playlist(
    id = "",
    title = "",
    cover = "",
    description = "",
)