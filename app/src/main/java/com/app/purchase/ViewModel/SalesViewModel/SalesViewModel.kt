package com.app.purchase.ViewModel.SalesViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.purchase.Room.AllSalesReposetory
import com.app.purchase.Room.SalesRoomDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by Android on 12/8/2018.
 */
public class SalesViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private var salesEntities: AllSalesReposetory? = null
    var allSales: LiveData<List<SalesEntities>>? = null


    init {
        val salesDao = SalesRoomDatabase.getDataBase(application)?.allSalesDao()
        salesEntities = AllSalesReposetory(salesDao!!)
    }


    fun getAllValuesOf(string: String) {
        allSales = salesEntities?.allSales(string)
    }

    fun getAllTotalPrice(string: String) = scope.launch(Dispatchers.IO) {
        salesEntities?.getSomOfTotalPrice(string)
    }

    fun insert(sales: SalesEntities) = scope.launch(Dispatchers.IO) {
        salesEntities?.insert(sales)
    }


    fun Update(updateSale: SalesEntities) = scope.launch(Dispatchers.IO) {
        salesEntities?.update(updateSale)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}