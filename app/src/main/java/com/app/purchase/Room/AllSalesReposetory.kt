package com.app.purchase.Room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.app.purchase.Room.Dao.AllSalesDao
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities

/**
 * Created by Android on 12/8/2018.
 */
class AllSalesReposetory(private val allsalesDao: AllSalesDao) {
    fun allSales(string: String): LiveData<List<SalesEntities>> {
        return allsalesDao.getAllSales(string)

    }

    fun getSomOfTotalPrice(string: String): List<SalesEntities> {
        return allsalesDao.totalSumPrice(string)
    }

    @WorkerThread
    fun insert(salesDao: SalesEntities) {
        allsalesDao.insertAllSales(salesDao)
    }

    @WorkerThread
    fun update(salesDao: SalesEntities) {
        allsalesDao.updateAllSales(salesDao)
    }


}