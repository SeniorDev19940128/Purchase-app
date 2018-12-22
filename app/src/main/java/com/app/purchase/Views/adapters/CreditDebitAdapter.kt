package com.app.purchase.Views.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.Interfaces.ClickListners.OnDebitCreditItemClick
import com.app.purchase.Prefrences.CurrencyPrefs
import com.app.purchase.R
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.debit_single_item.view.*
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CreditDebitAdapter(var context: Context, var currency: String, var creditList: List<CreditDebitEntities>, var onDebitCreditItemClick: OnDebitCreditItemClick) : RecyclerView.Adapter<CreditDebitAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setValue(creditList[position], position, CurrencyPrefs(context).getInstance(context).getCurrency(context))
        holder.itemView.setOnClickListener { onDebitCreditItemClick.OnItemClick(creditList[position]) }

        holder.debitDateLayout.visibility = View.VISIBLE
        holder.debitDate.text =formatToYesterdayOrToday( creditList[position].CreditDebitDate)
        holder.debitPrice.text = CurrencyPrefs(context).getInstance(context).getCurrency(context).plus(creditList[position].CreditDebitPricePrice)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.debit_single_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return creditList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name_item_debit as TextView
        val price = itemView.item_Sale_price_debit as TextView
        val creditOrDebit = itemView.item_debit_or_credit as TextView
        val debitOrCreditNotes = itemView.debit_or_credit_notes as TextView
        val debitDateLayout = itemView.DebitDateLayout as LinearLayout
        val debitDate = itemView.DebitDate as TextView
        val debitPrice = itemView.DebitSumValue as TextView
        val purchaseImage = itemView.profile_image as CircleImageView

        fun setValue(creditDebitEntities: CreditDebitEntities, position: Int, currency: String) {
            name.text = creditDebitEntities.CreditDebitName
            price.text = currency.plus(creditDebitEntities.CreditDebitPricePrice)
            debitOrCreditNotes.text = creditDebitEntities.CreditDebitNotes
            creditOrDebit.text = creditDebitEntities.CreditOrDebit

            val imgFile = File(creditDebitEntities.imageDir)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                purchaseImage.setImageBitmap(myBitmap)

            }
        }

    }

    private fun filterListByDate(creditList: List<CreditDebitEntities>, string: String): Int {
        val totalPriceInList1 = creditList.filter { it.CreditDebitDate == string }
        return totalPriceInList1.asSequence().map { it.CreditDebitPricePrice }.sum()
    }

    fun addCreditSale(creditList: List<CreditDebitEntities>) {
        this.creditList = creditList
        notifyDataSetChanged()
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