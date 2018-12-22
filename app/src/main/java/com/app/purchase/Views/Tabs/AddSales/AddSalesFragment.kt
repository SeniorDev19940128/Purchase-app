package com.app.purchase.Views.Tabs.AddSales


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.Interfaces.ClickListners.OnSaveClickListner
import com.app.purchase.Interfaces.ClickListners.deleterAddSaleitem
import com.app.purchase.R
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import com.app.purchase.Views.adapters.AddSalesAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class AddSalesFragment() : Fragment(), View.OnClickListener, deleterAddSaleitem {


    private var onSaveClickListner: OnSaveClickListner? = null
    private var backImage: ImageView? = null

    constructor(onSaveClickListner: OnSaveClickListner) : this() {
        this.onSaveClickListner = onSaveClickListner
    }

    private var rootView: View? = null
    private lateinit var AddSaleLayoutButton: ImageView
    private lateinit var SaveSale: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var Salesgroyp: EditText
    private lateinit var addnewSaleEntitiesList: ArrayList<AddsalesViewEntities>
    private lateinit var recyclerAdapter: AddSalesAdapter
    private var onSaveClickListners: OnSaveClickListner? = null
    private var salesEntities: SalesEntities? = null
    private var title = ""
    private var DateAndTime = ""
    private var TTPrice: Int = 0
    private var ID: Int = 0
    private var Time = ""
    private var loadListOf = ""
    var dd: Long  =0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_sales, container, false)
        initViews(rootView)
        setUpRecyclerView()
        return rootView
    }


    private fun initViews(rootView: View?) {
        if (arguments != null) {
            salesEntities = arguments?.getParcelable("salesEnitities")
            loadListOf = arguments!!.getString("isSaleOrPurchase")!!
        }
        if (salesEntities != null) {
            title = salesEntities!!.SalesTitle
            DateAndTime = salesEntities!!.salesDate
            TTPrice = salesEntities!!.totalSalesPrice
            ID = salesEntities!!.SalesId
            Time = salesEntities!!.salesTime
        }

        AddSaleLayoutButton = rootView!!.findViewById(R.id.AddSaleLayout)
        AddSaleLayoutButton.setOnClickListener(this)
        SaveSale = rootView.findViewById(R.id.SaveSale)
        SaveSale.setOnClickListener(this)
        backImage = rootView.findViewById(R.id.backImage)
        backImage?.setOnClickListener(this)
        recyclerView = rootView.findViewById(R.id.AddSalesProductList)
        Salesgroyp = rootView.findViewById(R.id.salesgroyp)
    }

    private fun setUpRecyclerView() {
        addnewSaleEntitiesList = ArrayList()
        addnewSaleEntitiesList.clear()

        recyclerAdapter = AddSalesAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter

        if (salesEntities != null) {
            addnewSaleEntitiesList.addAll(salesEntities?.totalsales as ArrayList<AddsalesViewEntities>)
            recyclerAdapter.addItems(true, addnewSaleEntitiesList)

            Salesgroyp.setText(salesEntities!!.SalesTitle)


            title = Salesgroyp.text.toString()

//            recyclerAdapter.notifyDataSetChanged()
        } else {
            recyclerAdapter.addItems(false, addnewSaleEntitiesList)
        }




    }


    private fun getCurretDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy     hh:mm")
        val currentDate = sdf.format(Date())
        if (DateAndTime.isEmpty()) {
            DateAndTime = currentDate
        }
        return DateAndTime
    }


    fun generateID(size: Int): String {
        val source = UUID.randomUUID().toString()
        val id = (source).map { it }.subList(0, size).joinToString("")
        if (title.isEmpty()) {
            title = id
        }
        return title
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.AddSaleLayout -> {
                var continueSize = 0

                title = Salesgroyp.text.toString()

                val listItems: List<AddsalesViewEntities> = recyclerAdapter.getListOfItems()
                for (listItem in listItems) {
                    if (checkFields(title,listItem.Product, listItem.UnitPrice, listItem.QTE, listItem.TTPrice)) {
                        if (listItems.size - 1 == continueSize) {
                            recyclerAdapter.addItems(false, addnewSaleEntitiesList)
                        }
                        continueSize++
                        recyclerView.scrollToPosition(recyclerAdapter.itemCount - 1)
                    } else {
                        Toast.makeText(context, "Cant leave empty field's", Int.MIN_VALUE).show()
                    }
                }
            }
            R.id.SaveSale -> {
                addsales()
            }
            R.id.backImage -> {
                fragmentManager?.popBackStack()
            }
        }
    }

    private fun addsales() {
        var continueSize = 0
        val listItems: List<AddsalesViewEntities> = recyclerAdapter.getListOfItems()
        val totalPriceInList1: Int = listItems.asSequence().map { it.TTPrice }.sum()
        val date = getCurretDateTime().split(" ")
        if (Time.isEmpty()) {
            Time = date[5]
        }
        title = Salesgroyp.text.toString()

        dd  = System.currentTimeMillis()
        for (listOfItem in listItems) {
            if (checkFields(title,listOfItem.Product, listOfItem.UnitPrice, listOfItem.QTE, listOfItem.TTPrice)) {
                if (listItems.size - 1 == continueSize) {
                    hideKeyboard()
                    onSaveClickListner?.SaveData(SalesEntities(ID, loadListOf, title, date[0], Time, totalPriceInList1, listItems))
                    activity?.supportFragmentManager?.popBackStack()


                }
                continueSize++
            } else {
                Toast.makeText(context, "Cant leave empty field's", Int.MIN_VALUE).show()
            }

        }
    }


    private fun checkFields(title : String ,product: String, unitPrice: Int, qte: Int, TtPrice: Int): Boolean {
        var returns = false
        if (title != "" && product != "" && unitPrice != 0 && qte != 0 && TtPrice != 0) {
            returns = true
        } else {
            false
        }
        return returns
    }
    private fun checkgFields(product: String, unitPrice: Int, qte: Int, TtPrice: Int): Boolean {
        var returns = false
        if (product != "" && unitPrice != 0 && qte != 0 && TtPrice != 0) {
            returns = true
        } else {
            false
        }
        return returns
    }

    override fun deleteItemOnPosition(position: Int, arraysize: Int) {
        if (position == 0) {
            Toast.makeText(context, "can not delete ", Int.MIN_VALUE).show()
        } else {
            val listItems: List<AddsalesViewEntities> = recyclerAdapter.getListOfItems()
            if (checkgFields(listItems[position].Product,listItems[position].UnitPrice,listItems[position].QTE,listItems[position].TTPrice))
            {
                showDialog(position)

            }
            else
            {
                recyclerAdapter.removeItem(position)


            }


        }
    }

    private fun showDialog(position: Int) {
        val dialogue = AlertDialog.Builder(context)
        dialogue.setTitle("Alert!")
        dialogue.setMessage("Are you sure to want delete this item")
        dialogue.setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
            //            addnewSaleEntitiesList.removeAt(position)
            recyclerAdapter.removeItem(position)
        }
        dialogue.setNegativeButton("No", null)
        val showDialog: AlertDialog = dialogue.create()
        showDialog.show()
    }

    private fun Fragment.hideKeyboard() {
        activity.hideKeyboard(view)
    }

    private fun FragmentActivity?.hideKeyboard(view: View?) {
        val inputMethodManager = this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}


