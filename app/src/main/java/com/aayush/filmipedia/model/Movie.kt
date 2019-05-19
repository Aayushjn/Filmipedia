package com.aayush.filmipedia.model

import android.os.Parcel
import android.os.Parcelable
import com.aayush.filmipedia.util.KParcelable
import com.aayush.filmipedia.util.readNullable
import com.aayush.filmipedia.util.writeNullable
import com.google.gson.annotations.SerializedName

data class Movie(@SerializedName("id") var id: Long? = null,
                 @SerializedName("title") var title: String? = null,
                 @SerializedName("vote_average") var voteAverage: Double? = null,
                 @SerializedName("poster_path") var posterPath: String? = null,
                 @SerializedName("adult") var isAdult: Boolean = false,
                 @SerializedName("overview") var overview: String? = null,
                 @SerializedName("release_date") var releaseDate: String? = null,
                 @SerializedName("genre_ids") var genreIds: List<Long>? = null,
                 @SerializedName("original_title") var originalTitle: String? = null,
                 @SerializedName("original_language") var originalLanguage: String? = null,
                 @SerializedName("backdrop_path") var backdropPath: String? = null,
                 @SerializedName("popularity") var popularity: Double? = null,
                 @SerializedName("vote_count") var voteCount: Long? = null,
                 @SerializedName("video") var isVideo: Boolean = false): KParcelable {
    private constructor(`in`: Parcel): this(
        `in`.readNullable { `in`.readLong() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readDouble() },
        `in`.readNullable { `in`.readString() },
        `in`.readInt() == 1,
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readArray(Long::class.java.classLoader)?.asList()?.filterIsInstance<Long>() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readString() },
        `in`.readNullable { `in`.readDouble() },
        `in`.readNullable { `in`.readLong() },
        `in`.readInt() == 1
    )

    constructor(cast: Cast): this(
        cast.id,
        cast.title,
        cast.voteAverage,
        cast.posterPath,
        cast.adult!!,
        cast.overview,
        cast.releaseDate,
        cast.genreIds,
        cast.originalTitle,
        cast.originalLanguage,
        cast.backdropPath,
        cast.popularity,
        cast.voteCount,
        cast.video!!
    )

    constructor(crew: Crew): this(
        crew.id,
        crew.title,
        crew.voteAverage,
        crew.posterPath,
        crew.adult!!,
        crew.overview,
        crew.releaseDate,
        crew.genreIds,
        crew.originalTitle,
        crew.originalLanguage,
        crew.backdropPath,
        crew.popularity,
        crew.voteCount,
        crew.video!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeNullable(id, ::writeLong)
        writeNullable(title, ::writeString)
        writeNullable(voteAverage, ::writeDouble)
        writeNullable(posterPath, ::writeString)
        writeInt(if (isAdult) 1 else 0)
        writeNullable(overview, ::writeString)
        writeNullable(releaseDate, ::writeString)
        writeNullable(genreIds, ::writeList)
        writeNullable(originalTitle, ::writeString)
        writeNullable(originalLanguage, ::writeString)
        writeNullable(backdropPath, ::writeString)
        writeNullable(popularity, ::writeDouble)
        writeNullable(voteCount, ::writeLong)
        writeInt(if (isVideo) 1 else 0)
    }

    companion object CREATOR: Parcelable.Creator<Movie> {
        override fun createFromParcel(source: Parcel) = Movie(source)
        override fun newArray(size: Int) = arrayOfNulls<Movie>(size)
    }
}
