package com.app.purchase.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.purchase.Room.CurrencyReposetory
import com.app.purchase.Room.SalesRoomDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var currencyEntity: CurrencyReposetory? = null
    var settingCurrencyValue: LiveData<CurrencyEntity>? = null

    init {
        val currency = SalesRoomDatabase.getDataBase(application)?.currency()
        currencyEntity = CurrencyReposetory(currency!!)
        settingCurrencyValue = currencyEntity?.getCurrency()
    }



    fun insertCurrency(currencyEntitys: CurrencyEntity) = scope.launch(Dispatchers.IO) {
        currencyEntity?.insertCurrency(currencyEntitys)
    }

}