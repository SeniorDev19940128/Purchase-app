package com.app.purchase.Views.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.Interfaces.ClickListners.RecyclerClick
import com.app.purchase.Interfaces.ClickListners.editSalesClick
import com.app.purchase.Prefrences.CurrencyPrefs
import com.app.purchase.R
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import kotlinx.android.synthetic.main.single_sale_item.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Android on 12/7/2018.
 */
class ShowSalesAdapter(var context: Context , var currency: String, var items: List<SalesEntities>, itemClick: RecyclerClick, editSalesClick: editSalesClick) : RecyclerView.Adapter<ShowSalesAdapter.ViewHolder>() {
    private var itemsList: List<SalesEntities> = items
    private var recyclerClick: RecyclerClick = itemClick
    private var editSalesClick: editSalesClick = editSalesClick
    private var Dates = ""
    private var previousDate = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.single_sale_item, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isNotEmpty()) {
            val item = items[position]
            holder.NAME.text = item.SalesTitle
            holder.DATE.text = formatToYesterdayOrToday(item.salesDate)
            holder.item_Time.text = item.salesTime
            holder.PRICE.text = CurrencyPrefs(context).getInstance(context).getCurrency(context).plus(item.totalSalesPrice)
            if (item.SaleOrPurchase == ("Sale")) {
                holder.salePurchaseIcon.setImageResource(R.drawable.s)
            } else {
                holder.salePurchaseIcon.setImageResource(R.drawable.p)
            }
            holder.EditClick.setOnClickListener { recyclerClick.OnItemClick(item) }
            holder.itemView.setOnClickListener { editSalesClick.OpenEditSalesScreen(item) }
            Dates = items[position].salesDate
            val date = Dates.split(" ")
            if (position == 0) {
                holder.ByDateSumLayout.visibility = View.VISIBLE
                holder.ByDate.text = formatToYesterdayOrToday(item.salesDate)
                previousDate = date[0]
                holder.ByDateSum.text =  CurrencyPrefs(context).getInstance(context).getCurrency(context).plus(filterListByDate())

            } else {
                if (previousDate == date[0]) {
                    holder.ByDateSumLayout.visibility = View.GONE
                } else {
                    holder.ByDateSumLayout.visibility = View.VISIBLE
                    holder.ByDate.text = formatToYesterdayOrToday(item.salesDate)
                    previousDate = date[0]
                    holder.ByDateSum.text =  CurrencyPrefs(context).getInstance(context).getCurrency(context).plus(filterListByDate())
                }
            }
            if (position == items.size - 1) {
                Dates = items[0].salesDate
                previousDate = Dates.split(" ")[0]
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun filterListByDate(): Int {
        val totalPriceInList1 = items.filter { it.salesDate == previousDate }
        return totalPriceInList1.asSequence().map { it.totalSalesPrice }.sum()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NAME = itemView.name_item_sale as TextView
        var DATE = itemView.item_date as TextView
        var PRICE = itemView.item_Sale_price as TextView
        var EditClick = itemView.EditClick as LinearLayout
        var ByDate = itemView.ByDate as TextView
        var ByDateSum = itemView.ByDateSumValue as TextView
        var ByDateSumLayout = itemView.ByDateLayout as LinearLayout
        var item_Time = itemView.item_Time as TextView
        var salePurchaseIcon = itemView.iconSaleAndPurchase as ImageView

    }

    fun setListItems(salesViewModel: List<SalesEntities>) {
        this.items = salesViewModel
        notifyDataSetChanged()
    }



    fun getListOfItems(): List<SalesEntities> {
        return itemsList
    }

    @Throws(ParseException::class)
    fun formatToYesterdayOrToday(date: String): String {
        val dateTime = SimpleDateFormat("dd/MM/yyyy").parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = dateTime
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        val timeFormatter = SimpleDateFormat("hh:mma")

        return if (calendar.get(Calendar.YEAR) === today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) === today.get(Calendar.DAY_OF_YEAR)) {
            "Today "
        } else if (calendar.get(Calendar.YEAR) === yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) === yesterday.get(Calendar.DAY_OF_YEAR)) {
            "Yesterday "
        } else {
            date
        }
    }

}