package com.example.keepnotes.models

import android.os.Parcel
import android.os.Parcelable

data class Notes(
    val note_id: Int = -1,
    val title: String? = null,
    val description: String? = null,
    val imageArray: ArrayList<String>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(note_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(imageArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notes> {
        override fun createFromParcel(parcel: Parcel): Notes {
            return Notes(parcel)
        }

        override fun newArray(size: Int): Array<Notes?> {
            return arrayOfNulls(size)
        }
    }
}