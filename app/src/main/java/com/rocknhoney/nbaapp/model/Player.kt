package com.rocknhoney.nbaapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.rocknhoney.nbaapp.roomdb.converters.Converters

@Entity(tableName = "players")
@TypeConverters(Converters::class)

data class Player(
    @SerializedName("id") @PrimaryKey val id: Long,
    @SerializedName("first_name") @ColumnInfo(name = "first_name") val firstName: String?,
    @SerializedName("last_name") @ColumnInfo(name = "last_name") val lastName: String?,
    @SerializedName("height_feet") @ColumnInfo(name = "height_feet") val heightFeet: String?,
    @SerializedName("height_inches") @ColumnInfo(name = "height_inches") val heightInches: String?,
    @SerializedName("position") @ColumnInfo(name = "position")  val position: String?,
    @SerializedName("team") @ColumnInfo(name = "team") val team: Team?,
    @SerializedName("weight_pounds") @ColumnInfo(name = "weight_pounds") val weightPounds: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Team::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(heightFeet)
        parcel.writeString(heightInches)
        parcel.writeString(position)
        parcel.writeParcelable(team, flags)
        parcel.writeString(weightPounds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}
