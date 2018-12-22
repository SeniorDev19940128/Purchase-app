package com.app.purchase.ViewModel.CreditDebit

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CreditDebit")
data class CreditDebitEntities
(@PrimaryKey(autoGenerate = true)
 val CreditDebitId: Int,
 @ColumnInfo(name = "CreditDebitName")
 var CreditDebitName: String,
 @ColumnInfo(name = "salesDate")
 var CreditDebitDate: String,
 @ColumnInfo(name = "CreditDebitPricePrice")
 var CreditDebitPricePrice: Int,
 var imageDir: String,
 var CreditOrDebit: String,
 var CreditDebitNotes: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(CreditDebitId)
        parcel.writeString(CreditDebitName)
        parcel.writeString(CreditDebitDate)
        parcel.writeInt(CreditDebitPricePrice)
        parcel.writeString(imageDir)
        parcel.writeString(CreditOrDebit)
        parcel.writeString(CreditDebitNotes)
    }

    override fun describeContents(): Int {
        return describeContents()
    }

    companion object CREATOR : Parcelable.Creator<CreditDebitEntities> {
        override fun createFromParcel(parcel: Parcel): CreditDebitEntities {
            return CreditDebitEntities(parcel)
        }

        override fun newArray(size: Int): Array<CreditDebitEntities?> {
            return arrayOfNulls(size)
        }
    }

}