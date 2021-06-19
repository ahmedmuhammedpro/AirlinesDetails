package com.ahmed.airlinesdetails.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlin.math.log

data class AirlinesListResponse(@SerializedName("items") val items: List<Airline>) : BaseResponse()

data class AirlineResponse(@SerializedName("value") val airline: Airline) : BaseResponse()

class Airline(
        val id: String?, val name: String, val country: String, val logo: String,
        val slogan: String, @SerializedName("head_quaters") val headQuarters: String,
        val website: String, val established: String): Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: ""
        )

        override fun describeContents(): Int {
                return 0
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.writeString(id)
                dest?.writeString(name)
                dest?.writeString(country)
                dest?.writeString(logo)
                dest?.writeString(slogan)
                dest?.writeString(headQuarters)
                dest?.writeString(website)
                dest?.writeString(established)
        }

        companion object CREATOR : Parcelable.Creator<Airline> {
                override fun createFromParcel(parcel: Parcel): Airline {
                        return Airline(parcel)
                }

                override fun newArray(size: Int): Array<Airline?> {
                        return arrayOfNulls(size)
                }
        }

}
