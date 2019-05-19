package com.aayush.filmipedia.model

import android.os.Parcel
import android.os.Parcelable
import com.aayush.filmipedia.util.KParcelable
import com.aayush.filmipedia.util.readNullable
import com.aayush.filmipedia.util.writeNullable
import com.google.gson.annotations.Expose

data class Genre(@Expose var id: Long?, @Expose var name: String?): KParcelable {
    private constructor(`in`: Parcel): this(
        `in`.readNullable { `in`.readLong() },
        `in`.readNullable { `in`.readString() }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeNullable(id, ::writeLong)
        writeNullable(name, ::writeString)
    }

    companion object CREATOR: Parcelable.Creator<Genre> {
        override fun createFromParcel(source: Parcel) = Genre(source)
        override fun newArray(size: Int) = arrayOfNulls<Genre>(size)
    }
}
