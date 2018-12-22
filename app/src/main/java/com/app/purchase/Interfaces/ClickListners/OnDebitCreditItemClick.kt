package com.app.purchase.Interfaces.ClickListners

import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities

interface OnDebitCreditItemClick {
    fun OnItemClick(debitEntities: CreditDebitEntities)
}