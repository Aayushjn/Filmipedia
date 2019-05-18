package com.aayush.filmipedia.model

import android.os.Parcel
import android.os.Parcelable
import com.aayush.filmipedia.util.KParcelable
import com.aayush.filmipedia.util.readNullable
import com.aayush.filmipedia.util.writeNullable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Person(@Expose var adult: Boolean? = null,
                  @SerializedName("also_known_as") var alsoKnownAs: List<String>? = null,
                  @Expose var biography: String? = null,
                  @Expose var birthday: String? = null,
                  @Expose var deathday: String? = null,
                  @Expose var gender: Int = 0,
                  @Expose var homepage: String? = null,
                  @Expose var id: Long? = null,
                  @SerializedName("imdb_id") var imdbId: String? = null,
                  @SerializedName("known_for_department") var knownForDepartment: String? = null,
                  @Expose var name: String? = null,
                  @SerializedName("place_of_birth") var placeOfBirth: String? = null,
                  @Expose var popularity: Double? = null,
                  @SerializedName("profile_path") var profilePath: String? = null) : KParcelable {
    private constructor(`in`: Parcel): this(
        `in`.readNullable { `in`.readInt() == 1 },
        `in`.readNullable { `in`.readArrayList(List::class.java.classLoader)?.filterIsInstance<String>() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readInt(),
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readLong() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readDouble() },
        `in`.readNullable { `in`.readString() }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(if (adult!!) 1 else 0)
        writeNullable(alsoKnownAs, ::writeList)
        writeNullable(biography, ::writeString)
        writeNullable(birthday, ::writeString)
        writeNullable(deathday, ::writeString)
        writeInt(gender)
        writeNullable(homepage, ::writeString)
        writeNullable(id, ::writeLong)
        writeNullable(imdbId, ::writeString)
        writeNullable(knownForDepartment, ::writeString)
        writeNullable(name, ::writeString)
        writeNullable(placeOfBirth, ::writeString)
        writeNullable(popularity, ::writeDouble)
        writeNullable(profilePath, ::writeString)
    }

    companion object CREATOR: Parcelable.Creator<Person> {
        override fun createFromParcel(source: Parcel) = Person(source)
        override fun newArray(size: Int) = arrayOfNulls<Person>(size)
    }
}
