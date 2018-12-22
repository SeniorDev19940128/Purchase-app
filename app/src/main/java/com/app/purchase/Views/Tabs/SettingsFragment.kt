package com.app.purchase.Views.Tabs


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.purchase.Prefrences.CurrencyPrefs
import com.app.purchase.R


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {


    private var currencyList: ArrayList<String>? = null
    private var roomview: View? = null
    private var currencySpinner: Spinner? = null
    private var backImageView: ImageView? = null
    private var currency_toggle: ToggleButton? = null
    private var spinnerSelectedPos = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        roomview = inflater.inflate(R.layout.fragment_settings, container, false)
        initViews(roomview)

        return roomview
    }

    private fun initViews(roomview: View?) {
        currencyList = ArrayList()
        currencySpinner = roomview?.findViewById(R.id.CurrencySpinner)
        currencySpinner?.onItemSelectedListener = this
        backImageView = roomview?.findViewById(R.id.backImage)
        currency_toggle = roomview?.findViewById(R.id.currency_toggle)
        currency_toggle?.setOnCheckedChangeListener(this)
        backImageView?.setOnClickListener(this)
        currencyList?.add("Currency")
        currencyList?.add("£ Pound")
        currencyList?.add("¥ Chinese yuan")
        currencyList?.add("$ US Dollar")
        currencyList?.add("€ Euro")
        currencyList?.add("UAE Dirham")
        currencyList?.add("PKR Pakistan")
        spinnerSelectedPos = CurrencyPrefs(context!!).getInstance(context!!).getSelectedCurrency(context!!)
        spinnerAdapter(currencyList, currencySpinner, spinnerSelectedPos)
        val isTrue = CurrencyPrefs(context!!).getInstance(context!!).getToggleValue(context!!)
        currencySpinner?.isEnabled = isTrue
        currency_toggle?.isChecked = isTrue

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.currency_toggle -> {
                if (isChecked) {
                    CurrencyPrefs(context!!).getInstance(context!!).setCurrency(context!!, "")
                    sendMessage()
                    currencySpinner?.isEnabled = true
                    CurrencyPrefs(context!!).getInstance(context!!).setToggleOnOff(context!!, isChecked)

                } else {
                    currencySpinner?.isEnabled = false
                    CurrencyPrefs(context!!).getInstance(context!!).setToggleOnOff(context!!, isChecked)
                    CurrencyPrefs(context!!).getInstance(context!!).setCurrency(context!!, "")
                    CurrencyPrefs(context!!).getInstance(context!!).setSelectedCurrency(context!!,0)

                }

                sendMessage()


            }
        }
    }

    private fun spinnerAdapter(arrayList: ArrayList<*>?, spinner: Spinner?, selectPos: Int) {
        if (arrayList != null) {
            try {
                val adapter = ArrayAdapter(context!!, R.layout.spinner_layout, arrayList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner!!.adapter = adapter
                spinner.setSelection(selectPos)
            } catch (e: NullPointerException) {
                Log.d(TAG, "spinnerAdapter: " + e.message)
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent?.selectedItem != "Currency") {
            val currency = parent?.selectedItem.toString().split(" ")
            CurrencyPrefs(context!!).getInstance(context!!).setCurrency(context!!, currency[0])
            CurrencyPrefs(context!!).getInstance(context!!).setSelectedCurrency(context!!,position)
            sendMessage()
        }
    }

    private fun sendMessage() {
        val intent = Intent("custom-currency-even")
        // You can also include some extra data.
        intent.putExtra("message", "change currency")
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backImage -> {
                if (!fragmentManager!!.executePendingTransactions()) {
                    fragmentManager!!.popBackStackImmediate()
                }
            }
        }
    }


}
