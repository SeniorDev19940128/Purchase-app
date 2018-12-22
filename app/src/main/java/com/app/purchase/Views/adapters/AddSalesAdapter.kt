package com.app.purchase.Views.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.CommonClasses.FontAwesome
import com.app.purchase.Interfaces.ClickListners.deleterAddSaleitem
import com.app.purchase.R
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import kotlinx.android.synthetic.main.add_sale_product_layout.view.*

/**
 * Created by Android on 12/6/2018.
 */
class AddSalesAdapter() : RecyclerView.Adapter<AddSalesAdapter.ViewHolder>() {
    private var itemsList: ArrayList<AddsalesViewEntities>? = null
    private var deleterAddSaleitem: deleterAddSaleitem? = null
    var b :Boolean = false

    constructor(deleterAddSaleitem: deleterAddSaleitem) : this() {
        itemsList = ArrayList()
        this.deleterAddSaleitem = deleterAddSaleitem
    }

    override fun getItemCount(): Int {
        return itemsList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.add_sale_product_layout, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(itemsList!![position])

        var uPrice: Int
        var quantity: Int
        var sum: Int
        holder.product.afterTextChanged {
            itemsList!![position].Product = it


        }

        holder.unitPrice.afterTextChanged {

                    if (it.isNotEmpty()) {
                itemsList!![position].UnitPrice = it.toInt()
                sum = it.toInt() * itemsList!![position].QTE
                holder.tTPrice.setText("".plus(sum))
                itemsList!![position].TTPrice = sum
            } else {
                uPrice = 0
                sum = uPrice * itemsList!![position].QTE
                holder.tTPrice.setText("".plus(sum))
                itemsList!![position].TTPrice = sum
                itemsList!![position].UnitPrice = 0
            }
        }
        holder.qTE.afterTextChanged {

            if (it.isNotEmpty()) {
                itemsList!![position].QTE = it.toInt()
                sum = itemsList!![position].UnitPrice * it.toInt()
                holder.tTPrice.setText("".plus(sum))
                itemsList!![position].TTPrice = sum
            } else {
                quantity = 0
                sum = itemsList!![position].UnitPrice * itemsList!![position].QTE
                holder.tTPrice.setText("".plus(sum))
                itemsList!![position].TTPrice = sum
                itemsList!![position].QTE = 0
            }
        }


        holder.tTPrice.afterTextChanged {
            if (it.isNotEmpty()) {
                itemsList!![position].TTPrice = it.toInt()
            } else {

                    sum = itemsList!![position].UnitPrice *itemsList!![position].QTE
                    holder.tTPrice.setText("".plus(sum))
                    itemsList!![position].TTPrice = sum


            }



    }
        holder.iteDelete.setOnClickListener {
            deleterAddSaleitem!!.deleteItemOnPosition(position, itemsList?.size!!.minus(1))
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val qTE = itemView?.QTE as EditText
        val unitPrice = itemView?.UnitPrice as EditText
        val tTPrice = itemView?.TTPrice as EditText
        val product = itemView?.Product as EditText
        val iteDelete = itemView?.saleItemDelete as FontAwesome
        fun bindItems(addSaleViewEntities: AddsalesViewEntities) {


            product.setText(addSaleViewEntities.Product)
             if (addSaleViewEntities.UnitPrice != 0) {
                unitPrice.setText("".plus(addSaleViewEntities.UnitPrice))
            }
            if (addSaleViewEntities.QTE != 0) {
                qTE.setText("".plus(addSaleViewEntities.QTE))
            }
            if (addSaleViewEntities.TTPrice != 0) {
                tTPrice.setText("".plus(addSaleViewEntities.TTPrice))
            }


        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    fun getListOfItems(): List<AddsalesViewEntities> {
        return itemsList!!
    }

    fun addItems(isAddAllList: Boolean, allListItems: ArrayList<AddsalesViewEntities>) {
        if (isAddAllList) {
            b = false
            itemsList?.addAll(allListItems)
        } else {
            b = true
            itemsList?.add(AddsalesViewEntities(0, "", 0, 0, "",TTPrice = 0))
        }
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {

        itemsList?.removeAt(position)
        notifyItemRemoved(position)
    }

}


