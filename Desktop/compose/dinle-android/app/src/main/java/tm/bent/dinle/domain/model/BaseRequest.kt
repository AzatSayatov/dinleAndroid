package tm.bent.dinle.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable

@Immutable
data class BaseRequest(
    var page :Int? = null,
    var pageSize :Int? = null,
    var search :String? = null,
    var artistId :String? = null,
    var songId: String? = null,
    var playlistId :String? = null,
    var albomId :String? = null,
    var showId: String? = null,
    var clipId: String? = null,
    var genreId :String? = null,
    var isLiked :Boolean? = null,
    var download: Boolean? = null,
    var following: Boolean? = null

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(page)
        parcel.writeValue(pageSize)
        parcel.writeString(search)
        parcel.writeString(artistId)
        parcel.writeString(songId)
        parcel.writeString(playlistId)
        parcel.writeString(albomId)
        parcel.writeString(showId)
        parcel.writeString(clipId)
        parcel.writeString(genreId)
        parcel.writeValue(isLiked)
        parcel.writeValue(following)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseRequest> {
        override fun createFromParcel(parcel: Parcel): BaseRequest {
            return BaseRequest(parcel)
        }

        override fun newArray(size: Int): Array<BaseRequest?> {
            return arrayOfNulls(size)
        }
    }

}

