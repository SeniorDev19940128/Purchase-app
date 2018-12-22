package com.app.purchase.ViewModel.TypeConverter

import androidx.room.TypeConverter
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Android on 12/8/2018.
 */
class TypeConverterForSales {
    @TypeConverter
    fun fromAddSalesJson(stat: List<AddsalesViewEntities>): String {
        return Gson().toJson(stat)
    }

    /**
     * Convert a json to a list of Images
     */
    @TypeConverter
    fun toAddSaleList(jsonImages: String): List<AddsalesViewEntities> {
        val notesType = object : TypeToken<List<AddsalesViewEntities>>() {}.type
        return Gson().fromJson<List<AddsalesViewEntities>>(jsonImages, notesType)
    }
}