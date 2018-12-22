package com.app.purchase.ViewModel.AddSaleViewModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Android on 12/6/2018.
 */

//Add Sales
@Entity(tableName = "AddSales")
data class AddsalesViewEntities(
        @PrimaryKey(autoGenerate = true)
        val addSalesId: Long = 0,
        var Product: String ="",
        var UnitPrice: Int,
        var QTE: Int,
        var SalesTitle: String,
        @ColumnInfo(name = "tTPrice")
        var TTPrice: Int)





