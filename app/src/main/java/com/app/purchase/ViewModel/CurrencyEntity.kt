package com.app.purchase.ViewModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
class CurrencyEntity(
        @PrimaryKey var id: Long,
        @ColumnInfo var Currency: String)