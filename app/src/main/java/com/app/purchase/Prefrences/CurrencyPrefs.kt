package com.app.purchase.Prefrences

import android.content.Context
import android.content.SharedPreferences

class CurrencyPrefs(var context: Context) {
    private val APP_CURRENCY_SETTINGS = "APP_CURRENCY_SETTINGS"
    private var mInstance: CurrencyPrefs? = null
    private var mCtx = context
    private val Currency = "Currency"
    private val Toggle = "Toggle"
    private val Position = "Position"


    @Synchronized
    fun getInstance(context: Context): CurrencyPrefs {
        if (mInstance == null) {
            mInstance = CurrencyPrefs(context)
        }
        return mInstance as CurrencyPrefs
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_CURRENCY_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getCurrency(context: Context): String {
        return getSharedPreferences(context).getString(Currency, "")!!
    }

    fun getToggleValue(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(Toggle, false)
    }

    fun getSelectedCurrency(context: Context): Int {
        return getSharedPreferences(context).getInt(Position, 0)
    }

    fun setSelectedCurrency(context: Context, position: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(Position, position)
        editor.apply()
        editor.commit()
    }


    fun setToggleOnOff(context: Context, ischecked: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Toggle, ischecked)
        editor.apply()
        editor.commit()
    }

    fun setCurrency(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Currency, newValue)
        editor.apply()
        editor.commit()
    }


}