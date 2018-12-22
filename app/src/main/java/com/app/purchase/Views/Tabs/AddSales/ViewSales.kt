package com.app.purchase.Views.Tabs.AddSales


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.R
import com.app.purchase.ViewModel.AddSaleViewModel.AddsalesViewEntities
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import com.app.purchase.Views.adapters.ViewSalesAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ViewSales : Fragment() ,View.OnClickListener{
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backImage -> {
                fragmentManager?.popBackStack()
            }
        }
    }

    private var rootview: View? = null
    private var salesRecyclerView: RecyclerView? = null
    private lateinit var viewSaleEntitiesList: ArrayList<AddsalesViewEntities>
    private var salesEntities: SalesEntities? = null
    private var recyclerAdapter: ViewSalesAdapter? = null
    private var saleOrPurchase: TextView? = null
    private var date: TextView? = null
    private var time: TextView? = null
    private var totalAmount: TextView? = null
    private var backImage: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_view_sales, container, false)
        if (arguments != null)
            salesEntities = arguments?.getParcelable("salesEnitities")

        initView(rootview)
        return rootview
    }

    private fun initView(rootview: View?) {
        salesRecyclerView = rootview?.findViewById(R.id.SalesRecyclerView)
        saleOrPurchase = rootview?.findViewById(R.id.SalesOrPurchase)
        date = rootview?.findViewById(R.id.Date)
        time = rootview?.findViewById(R.id.Time)
        totalAmount = rootview?.findViewById(R.id.TotalAmount)
        backImage = rootview?.findViewById(R.id.backImage)
        backImage?.setOnClickListener(this)
        saleOrPurchase?.text = salesEntities?.SalesTitle
        date?.text = salesEntities?.salesDate
        time?.text = salesEntities?.salesTime
        totalAmount?.text = "$".plus(salesEntities?.totalSalesPrice)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        viewSaleEntitiesList = ArrayList()
        recyclerAdapter = ViewSalesAdapter(viewSaleEntitiesList)
        salesRecyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        salesRecyclerView!!.adapter = recyclerAdapter
        if (salesEntities != null)
            viewSaleEntitiesList.addAll(salesEntities?.totalsales as ArrayList<AddsalesViewEntities>)

    }

}
