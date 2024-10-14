package tm.bent.dinle.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Genre(
    val genreId: String,
    val cover: String,
    val name: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(genreId)
        parcel.writeString(cover)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Genre> {
        override fun createFromParcel(parcel: Parcel): Genre {
            return Genre(parcel)
        }

        override fun newArray(size: Int): Array<Genre?> {
            return arrayOfNulls(size)
        }
    }


    fun getImage(): String {
        return cover
    }

}

val mockGenre = Genre(
    genreId = "",
    name = "Genre",
    cover = "",
)