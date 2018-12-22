package com.app.purchase.Views.Tabs.Sales


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.purchase.Interfaces.ClickListners.OnSaveClickListner
import com.app.purchase.Interfaces.ClickListners.RecyclerClick
import com.app.purchase.Interfaces.ClickListners.Updateable
import com.app.purchase.Interfaces.ClickListners.editSalesClick
import com.app.purchase.Prefrences.CurrencyPrefs
import com.app.purchase.R
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import com.app.purchase.ViewModel.SalesViewModel.SalesViewModel
import com.app.purchase.Views.Tabs.AddSales.AddSalesFragment
import com.app.purchase.Views.Tabs.AddSales.ViewSales
import com.app.purchase.Views.adapters.ShowSalesAdapter


/**
 * A simple [Fragment] subclass.
 */
class Sale(var loadListOf: String) : Fragment(), View.OnClickListener, OnSaveClickListner, RecyclerClick, editSalesClick {


    private var rootview: View? = null
    private var salesRecyclerView: RecyclerView? = null
    private var addSalesButton: ImageView? = null
    private lateinit var showSaleModelList: List<SalesEntities>
    private lateinit var recyclerAdapter: ShowSalesAdapter
    private lateinit var salesViewModel: SalesViewModel
    var addSalesFragment: AddSalesFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_sale, container, false)
        initView(rootview)
        setUpRecyclerView()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                IntentFilter("custom-currency-even"))
        return rootview
    }

    override fun onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("message")
            if (message == "change currency") {
                recyclerAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun setUpRecyclerView() {
        showSaleModelList = ArrayList()
        recyclerAdapter = ShowSalesAdapter(context!!,"", showSaleModelList, this, this)
        salesRecyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        salesRecyclerView!!.adapter = recyclerAdapter

        salesViewModel = ViewModelProviders.of(this@Sale).get(SalesViewModel::class.java)
        salesViewModel.getAllValuesOf(loadListOf)
        salesViewModel.allSales?.observe(this, Observer { salesViewModel ->
            recyclerAdapter.setListItems(salesViewModel!!)
        })

    }


    private fun initView(rootview: View?) {
        addSalesFragment = AddSalesFragment(also {

        })
        salesRecyclerView = rootview?.findViewById(R.id.SalesRecyclerView)
        addSalesButton = rootview?.findViewById(R.id.AddSalesButton)
        addSalesButton?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id: Int = p0!!.id
        when (id) {
            R.id.AddSalesButton -> addFragment(AddSalesFragment(this@Sale))
        }
    }

    private fun addFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, passValueToAdd(fragment, loadListOf))?.addToBackStack(null)?.commit()
    }

    override fun SaveData(salesEntities: SalesEntities) {
        Log.d("TAG", "Sales: " + salesEntities.SalesTitle)
        salesViewModel.insert(salesEntities)

    }

    override fun OnItemClick(salesEntities: SalesEntities) {
        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, newInstance(salesEntities, AddSalesFragment(this@Sale), loadListOf))?.addToBackStack(null)?.commit()
    }

    override fun OpenEditSalesScreen(salesEntities: SalesEntities) {
        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, newInstance(salesEntities, ViewSales(), loadListOf))?.addToBackStack(null)?.commit()
    }


    companion object {

        @JvmStatic
        fun newInstance(salesEntities: SalesEntities, fragment: Fragment, listOf: String) = fragment.apply {
            arguments = Bundle().apply {
                putParcelable("salesEnitities", salesEntities)
                putString("isSaleOrPurchase", listOf)
            }
        }

        @JvmStatic
        fun passValueToAdd(fragment: Fragment, listOf: String) = fragment.apply {
            arguments = Bundle().apply {
                putString("isSaleOrPurchase", listOf)
            }
        }
    }



}
