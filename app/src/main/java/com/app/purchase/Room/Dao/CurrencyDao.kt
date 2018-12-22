package com.app.purchase.Room.Dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.purchase.ViewModel.CurrencyEntity

@Dao
interface CurrencyDao {
    @Query("SELECT * from Currency")
    fun getCurrency(): LiveData<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun insertCurrency(currencyEntity: CurrencyEntity)
}