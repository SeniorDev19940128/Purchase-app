package com.app.purchase.Views.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.R
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import kotlinx.android.synthetic.main.view_sales.view.*

class ViewSalesAdapter(listitem: ArrayList<AddsalesViewEntities>) : RecyclerView.Adapter<ViewSalesAdapter.ViewHolder>() {
    private var itemsList: ArrayList<AddsalesViewEntities> = listitem

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.view_sales, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val salesAdapter = itemsList[position]
        var value = ""
        if (salesAdapter.Product != "") {
            holder.Product.text = salesAdapter.Product
        }
        if (salesAdapter.UnitPrice != 0) {
            holder.UnitPrice.text = value.plus(salesAdapter.UnitPrice)
        }
        if (salesAdapter.QTE != 0) {
            holder.QTE.text = value.plus(salesAdapter.QTE)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val QTE = itemView?.QTE as TextView
        var UnitPrice = itemView?.UnitPrice as TextView
        var Product = itemView?.Product as TextView

    }

}