package com.aayush.filmipedia.model

import android.os.Parcel
import android.os.Parcelable
import com.aayush.filmipedia.util.KParcelable
import com.aayush.filmipedia.util.readNullable
import com.aayush.filmipedia.util.writeNullable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonResult(@Expose var popularity: Double? = null,
                        @Expose var id: Long? = null,
                        @SerializedName("profile_path") var profilePath: String? = null,
                        @Expose var name: String? = null,
                        @SerializedName("known_for") var movies: List<Movie>? = null,
                        @Expose var adult: Boolean? = null): KParcelable {
    private constructor(`in`: Parcel): this(
        `in`.readNullable { `in`.readDouble() },
        `in`.readNullable { `in`.readLong() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.createTypedArrayList(Movie.CREATOR) },
        `in`.readNullable { `in`.readInt() == 1 }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeNullable(popularity, ::writeDouble)
        writeNullable(id, ::writeLong)
        writeNullable(profilePath, ::writeString)
        writeNullable(name, ::writeString)
        writeNullable(movies, ::writeTypedList)
        writeInt(if (adult!!) 1 else 0)
    }
    
    companion object CREATOR: Parcelable.Creator<PersonResult> {
        override fun createFromParcel(source: Parcel) = PersonResult(source)
        override fun newArray(size: Int) = arrayOfNulls<PersonResult>(size)
    }
}
