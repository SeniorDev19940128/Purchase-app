package com.app.purchase.ViewModel.AddSaleViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.purchase.Room.AdSalesReposetory
import com.app.purchase.Room.SalesRoomDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by Android on 12/8/2018.
 */
class addSaleViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private var reposetory: AdSalesReposetory? = null
    var allSales: LiveData<List<AddsalesViewEntities>>? = null

//    init {
//        val salesDao = SalesRoomDatabase.getDataBase(application)?.salesDao()
//        reposetory = AdSalesReposetory(salesDao!!)
//        allSales = reposetory?.allSales
//
//    }
//
//    fun insert(sales: AddsalesViewEntities) = scope.launch(Dispatchers.IO) {
//        reposetory?.insert(sales)
//    }
//
//    fun Update(updateSale: AddsalesViewEntities) = scope.launch(Dispatchers.IO) {
//        reposetory?.update(updateSale)
//    }

//    fun getTotalCount(updateSale: List<AddsalesViewEntities>) = scope.launch(Dispatchers.IO) {
//        reposetory?.getTotal(updateSale)
//    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}