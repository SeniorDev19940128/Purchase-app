package com.app.purchase.Room.Dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities

@Dao
interface CreditDebitDao {
    @Query("SELECT * from CreditDebit")
    fun getAllSales(): LiveData<List<CreditDebitEntities>>

    @Query("SELECT * from CreditDebit where CreditOrDebit = :string")
    fun getFilterCreditDebitSales(string: String): LiveData<List<CreditDebitEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun insertAllSales(creditDebitEntities: CreditDebitEntities)
}