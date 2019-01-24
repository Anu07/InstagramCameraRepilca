package com.desmond.squarecamera.galleryView

import android.os.Parcel
import android.os.Parcelable

data class Albums(var folderNames: String, var imagePath: String, var imgCount: Int, var isVideo: Boolean) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(folderNames)
        parcel.writeString(imagePath)
        parcel.writeInt(imgCount)
        parcel.writeByte(if (isVideo) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Albums> {
        override fun createFromParcel(parcel: Parcel): Albums {
            return Albums(parcel)
        }

        override fun newArray(size: Int): Array<Albums?> {
            return arrayOfNulls(size)
        }
    }
}