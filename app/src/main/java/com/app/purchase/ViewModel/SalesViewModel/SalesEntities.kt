package com.app.purchase.ViewModel.SalesViewModel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import com.app.purchase.ViewModel.TypeConverter.TypeConverterForSales

/**
 * Created by Android on 12/8/2018.
 */
//Sales
@Entity(tableName = "Sales")
public class SalesEntities(
        @PrimaryKey(autoGenerate = true)
        val SalesId: Int,
        @ColumnInfo(name = "SaleOrPurchase")
        val SaleOrPurchase: String,
        @ColumnInfo(name = "SalesTitle")
        var SalesTitle: String,
        @ColumnInfo(name = "salesDate")
        var salesDate: String,
        var salesTime: String,
        @ColumnInfo(name = "totalSalesPrice")
        var totalSalesPrice: Int,
        @TypeConverters(TypeConverterForSales::class)
        var totalsales: List<AddsalesViewEntities>? = null) : Parcelable {

    override fun describeContents(): Int {
        return describeContents()
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt())

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(SalesId)
        p0?.writeString(SalesTitle)
        p0?.writeString(salesDate)
        p0?.writeString(salesTime)
        p0?.writeInt(totalSalesPrice)
    }

    companion object CREATOR : Parcelable.Creator<SalesEntities> {
        override fun createFromParcel(parcel: Parcel): SalesEntities {
            return SalesEntities(parcel)
        }

        override fun newArray(size: Int): Array<SalesEntities?> {
            return arrayOfNulls(size)
        }
    }
}