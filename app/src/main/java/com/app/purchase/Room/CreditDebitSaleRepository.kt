package com.app.purchase.Room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.app.purchase.Room.Dao.CreditDebitDao
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities

class CreditDebitSaleRepository(private val creditDebitDao: CreditDebitDao) {
    fun allSales(): LiveData<List<CreditDebitEntities>> {
        return creditDebitDao.getAllSales()
    }

    fun filterCreditDebit(string: String): LiveData<List<CreditDebitEntities>> {
        return creditDebitDao.getFilterCreditDebitSales(string)
    }

    @WorkerThread
    suspend fun insert(creditDebitEntities: CreditDebitEntities) {
        creditDebitDao.insertAllSales(creditDebitEntities)
    }

}