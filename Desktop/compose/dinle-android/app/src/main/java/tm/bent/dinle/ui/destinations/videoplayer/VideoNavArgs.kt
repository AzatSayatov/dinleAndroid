package tm.bent.dinle.ui.destinations.videoplayer

import android.os.Parcel
import android.os.Parcelable

data class VideoNavArgs(
    val videoUrls: List<String>, // Список URL
    val currentIndex: Int        // Текущий индекс
) : Parcelable {

    // Метод для сериализации объекта в Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(videoUrls) // Сериализуем список строк
        parcel.writeInt(currentIndex)     // Сериализуем индекс
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<VideoNavArgs> {

        // Метод для десериализации объекта из Parcel
        override fun createFromParcel(parcel: Parcel): VideoNavArgs {
            val videoUrls = parcel.createStringArrayList() ?: listOf() // Читаем список строк
            val currentIndex = parcel.readInt()                         // Читаем индекс
            return VideoNavArgs(videoUrls, currentIndex)
        }

        override fun newArray(size: Int): Array<VideoNavArgs?> = arrayOfNulls(size)
    }
}
