package com.app.purchase.Interfaces.ClickListners

import com.app.purchase.ViewModel.SalesViewModel.SalesEntities

/**
 * Created by Android on 12/10/2018.
 */
interface RecyclerClick {
    public fun OnItemClick(salesEntities: SalesEntities)
}