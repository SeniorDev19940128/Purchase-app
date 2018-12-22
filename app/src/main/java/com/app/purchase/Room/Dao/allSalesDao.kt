package com.app.purchase.Room.Dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities


/**
 * Created by Android on 12/8/2018.
 */
@Dao
interface AllSalesDao {
    @Query("SELECT * from Sales WHERE SaleOrPurchase = :string")
    fun getAllSales(string: String): LiveData<List<SalesEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun insertAllSales(sales: SalesEntities)

    @Query("SELECT /*SUM(totalSalesPrice) as total*/ * FROM Sales Where salesDate = :date")
    fun totalSumPrice(date: String): List<SalesEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun saveAll(items: List<SalesEntities>)


    @Update
    fun updateAllSales(addsalesViewModel: SalesEntities)

//    @Query("SELECT FROM Sales WHERE SalesTitle = columnName")
//    fun getSpecificRowFromTable(columnName: String)

}