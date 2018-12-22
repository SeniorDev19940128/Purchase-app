package com.app.purchase.ViewModel.CreditDebit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.purchase.Room.CreditDebitSaleRepository
import com.app.purchase.Room.SalesRoomDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

class CreditDebitViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private var creditDebitSaleRepository: CreditDebitSaleRepository? = null
    var allCreditDebit: LiveData<List<CreditDebitEntities>>? = null
    var allCreditDebitFiltersData: LiveData<List<CreditDebitEntities>>? = null

    init {
        val salesDao = SalesRoomDatabase.getDataBase(application)?.creditDebit()
        creditDebitSaleRepository = CreditDebitSaleRepository(salesDao!!)
        allCreditDebit = creditDebitSaleRepository?.allSales()
    }

    fun filterCreditDebit(string: String){
        allCreditDebitFiltersData = creditDebitSaleRepository?.filterCreditDebit(string)
    }


    fun insert(creditDebitEntities: CreditDebitEntities) = scope.launch(Dispatchers.IO) {
        creditDebitSaleRepository?.insert(creditDebitEntities)
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}