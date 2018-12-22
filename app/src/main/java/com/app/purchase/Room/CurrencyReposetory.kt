package com.app.purchase.Room

import androidx.lifecycle.LiveData
import com.app.purchase.Room.Dao.CurrencyDao
import com.app.purchase.ViewModel.CurrencyEntity

class CurrencyReposetory(private var getCurrencyDao: CurrencyDao) {
    fun getCurrency(): LiveData<CurrencyEntity> {
        return getCurrencyDao.getCurrency()
    }

    fun insertCurrency(currencyEntity: CurrencyEntity) {
        getCurrencyDao.insertCurrency(currencyEntity)
    }

}