package com.app.purchase.Views.stats


import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.purchase.R
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities
import com.app.purchase.ViewModel.CreditDebit.CreditDebitViewModel
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import com.app.purchase.ViewModel.SalesViewModel.SalesViewModel
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


/**
 * A simple [Fragment] subclass.
 */
open class Stats : Fragment() {

    private var rootView: View? = null
    private var MonthSpinner: Spinner? = null
    private var YearSpinner: Spinner? = null
    private var MonthList: ArrayList<MonthsWithId>? = null
    private var MonthStringList: ArrayList<String>? = null
    private var YearList: ArrayList<String>? = null
    private var chart: CombinedChart? = null
    private var salesViewModel: SalesViewModel? = null
    private var debitViewModel: CreditDebitViewModel? = null
    val entries = ArrayList<Entry>()
    private val purchaseEntries = ArrayList<Entry>()
    private val DebitEntries = ArrayList<Entry>()
    private val CreditEntries = ArrayList<Entry>()
    private var xAxis: XAxis? = null
    val data = CombinedData()
    private var year = ""
    private var month = ""


    protected val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_stats, container, false)
        initViews(rootView)

        salesViewModel = ViewModelProviders.of(this).get(SalesViewModel::class.java)
        debitViewModel = ViewModelProviders.of(this@Stats).get(CreditDebitViewModel::class.java)

        getAllValuesForGraph("", "")
        // draw bars behind lines
        chart?.drawOrder = arrayOf(CombinedChart.DrawOrder.LINE)
        val l = chart?.legend
        l?.isWordWrapEnabled = true
        l?.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l?.orientation = Legend.LegendOrientation.HORIZONTAL
        l?.setDrawInside(false)

        val rightAxis = chart?.xAxis
        rightAxis?.setDrawGridLines(false)
        rightAxis?.axisMinimum = 1f


        xAxis = chart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.axisMinimum = 0f
        xAxis?.granularity = 0f
        xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return months[value.toInt() % months.size]
            }
        }

        return rootView
    }

    private fun getAllValuesForGraph(month: String, year: String) {
        // --chain Query Observer for line graph--
        // get Sales
        salesViewModel?.getAllValuesOf("Sale")
        salesViewModel?.allSales?.observe(this, Observer { salesViewModels ->
            val salesList = salePurchaseValues(salesViewModels)
            entries.clear()
            for (index in 0 until salesList.size) {
                entries.add(Entry(index + 0.1f, salesList[index].totalSalesPrice.toFloat()))
            }
            // get Purchase
            salesViewModel?.getAllValuesOf("Purchase")
            salesViewModel?.allSales?.observe(this, Observer { purchaseModel ->
                val purchaseList = salePurchaseValues(purchaseModel)
                purchaseEntries.clear()
                for (index in 0 until purchaseList.size) {
                    purchaseEntries.add(Entry(index + 0.1f, purchaseList[index].totalSalesPrice.toFloat()))
                }
                //get Debit
                debitViewModel?.filterCreditDebit("Debit")
                debitViewModel?.allCreditDebitFiltersData?.observe(this, Observer { debit ->
                    val debitList = creditDebitValues(debit)
                    DebitEntries.clear()
                    for (index in 0 until debitList.size) {
                        DebitEntries.add(Entry(index + 0.1f, debitList[index].CreditDebitPricePrice.toFloat()))
                    }
                    // get Credit
                    debitViewModel?.filterCreditDebit("Credit")
                    debitViewModel?.allCreditDebitFiltersData?.observe(this, Observer { credit ->
                        val creditList = creditDebitValues(credit)
                        CreditEntries.clear()
                        for (index in 0 until creditList.size) {
                            CreditEntries.add(Entry(index + 0.1f, creditList[index].CreditDebitPricePrice.toFloat()))
                        }
                        data.setData(generateLineData())
                        xAxis?.axisMaximum = data.xMax + 0.25f
                        chart?.data = data
                        chart?.invalidate()
                    })
                })
            })
        })
    }

    private fun salePurchaseValues(values: List<SalesEntities>): List<SalesEntities> {
        val purchaseList: List<SalesEntities>? = if (month.isNotEmpty() && year.isNotEmpty()) {
            values.filter { it.salesDate.split("/")[1] == month && it.salesDate.split("/")[2] == year }
        } else if (month.isNotEmpty() || year.isNotEmpty()) {
            values.filter { it.salesDate.split("/")[1] == month || it.salesDate.split("/")[2] == year }
        } else {
            values
        }
        return purchaseList!!
    }

    private fun creditDebitValues(entities: List<CreditDebitEntities>): List<CreditDebitEntities> {
        val creditList: List<CreditDebitEntities>? = if (month.isNotEmpty() && year.isNotEmpty()) {
            entities.filter { it.CreditDebitDate.split("/")[1] == month && it.CreditDebitDate.split("/")[2] == year }
        } else if (month.isNotEmpty() || year.isNotEmpty()) {
            entities.filter { it.CreditDebitDate.split("/")[1] == month || it.CreditDebitDate.split("/")[2] == year }
        } else {
            entities
        }
        return creditList!!
    }

    private fun addLine(entries: ArrayList<Entry>, string: String, color: Int): LineDataSet {
        val set = LineDataSet(entries, string)
        set.color = color
        set.lineWidth = 4.5f
        set.setCircleColor(color)
        set.circleRadius = 1f
        set.setDrawCircleHole(true)
        set.setDrawCircles(true)
        set.fillColor = color
        set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 0f
        set.valueTextColor = color
        set.axisDependency = YAxis.AxisDependency.LEFT
        return set
    }


    private fun generateLineData(): LineData {
        val d = LineData()
        d.addDataSet(addLine(entries, "Sale", Color.rgb(240, 238, 70)))
        d.addDataSet(addLine(purchaseEntries, "Purchase", Color.rgb(222, 122, 70)))
        d.addDataSet(addLine(DebitEntries, "Debit", Color.rgb(32, 89, 70)))
        d.addDataSet(addLine(CreditEntries, "Credit", Color.rgb(123, 111, 70)))
        return d
    }


    private fun initViews(rootView: View?) {

        MonthSpinner = rootView?.findViewById(R.id.MonthSpinner)

        MonthList = ArrayList()
        MonthStringList = ArrayList()
        YearSpinner = rootView?.findViewById(R.id.YearSpinner)

        YearList = ArrayList()
        chart = rootView?.findViewById(R.id.chart1)
        chart?.description?.isEnabled = false
        chart?.setDrawGridBackground(false)
        chart?.setBorderColor(context?.resources?.getColor(R.color.white)!!)
        chart?.setTouchEnabled(false)
        chart?.setDrawBarShadow(false)
        chart?.isHighlightFullBarEnabled = false

        MonthList?.add(MonthsWithId("Month", "0"))
        MonthList?.add(MonthsWithId("Jan", "01"))
        MonthList?.add(MonthsWithId("Fab", "02"))
        MonthList?.add(MonthsWithId("Mar", "03"))
        MonthList?.add(MonthsWithId("Apr", "04"))
        MonthList?.add(MonthsWithId("May", "05"))
        MonthList?.add(MonthsWithId("Jun", "06"))
        MonthList?.add(MonthsWithId("Jul", "07"))
        MonthList?.add(MonthsWithId("Aug", "08"))
        MonthList?.add(MonthsWithId("Sep", "09"))
        MonthList?.add(MonthsWithId("Oct", "10"))
        MonthList?.add(MonthsWithId("Nov", "11"))
        MonthList?.add(MonthsWithId("Dev", "12"))

        MonthStringList?.add("Month")
        MonthStringList?.add("Jan")
        MonthStringList?.add("Fab")
        MonthStringList?.add("Mar")
        MonthStringList?.add("Apr")
        MonthStringList?.add("May")
        MonthStringList?.add("Jun")
        MonthStringList?.add("Jul")
        MonthStringList?.add("Aug")
        MonthStringList?.add("Sep")
        MonthStringList?.add("Oct")
        MonthStringList?.add("Nov")
        MonthStringList?.add("Dec")


        YearList?.add("Year")
        YearList?.add("2018")
        YearList?.add("2017")
        YearList?.add("2016")
        YearList?.add("2015")
        YearList?.add("2014")
        YearList?.add("2013")
        YearList?.add("2012")
        YearList?.add("2011")
        YearList?.add("2010")
        YearList?.add("2009")
        YearList?.add("2008")
        YearList?.add("2007")

        spinnerAdapter(MonthStringList, MonthSpinner, 12)
        spinnerAdapter(YearList, YearSpinner, 1)




        MonthSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent?.selectedItem != "Month") {
                    month = MonthList!![position].id
                    getAllValuesForGraph(month, year)
                }
            }

        }
        YearSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent?.selectedItem.toString() != "Year") {
                    year = parent?.selectedItem.toString()
                    getAllValuesForGraph(month, year)
                }
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
                Log.d(ContentValues.TAG, "spinnerAdapter: " + e.message)
            }

        }
    }


    data class MonthsWithId(var MonthValue: String, var id: String)
}
