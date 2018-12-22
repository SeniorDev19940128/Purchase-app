package com.app.purchase.ViewModel

import androidx.room.ColumnInfo


data class TotalSumPrice(
        @ColumnInfo(name = "tTPrice")
        private var TotalPrice: Int)