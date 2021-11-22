package com.rocknhoney.nbaapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "teams")
data class Team(
    @SerializedName("id") @PrimaryKey val id: Long,
    @SerializedName("abbreviation") @ColumnInfo(name = "abbreviation") val abbreviation: String?,
    @SerializedName("city") @ColumnInfo(name = "city") val city: String?,
    @SerializedName("conference") @ColumnInfo(name = "conference") val conference: String?,
    @SerializedName("division") @ColumnInfo(name = "division") val division: String?,
    @SerializedName("full_name") @ColumnInfo(name = "full_name") val fullName: String?,
    @SerializedName("name") @ColumnInfo(name = "name") val name: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(abbreviation)
        parcel.writeString(city)
        parcel.writeString(conference)
        parcel.writeString(division)
        parcel.writeString(fullName)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Team> {
        override fun createFromParcel(parcel: Parcel): Team {
            return Team(parcel)
        }

        override fun newArray(size: Int): Array<Team?> {
            return arrayOfNulls(size)
        }
    }
}
