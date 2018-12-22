package com.app.purchase.Views.Tabs.Debit

import android.content.*
import android.os.Bundle
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
import com.app.purchase.Interfaces.ClickListners.DebitCreditSaveClick
import com.app.purchase.Interfaces.ClickListners.OnDebitCreditItemClick
import com.app.purchase.Interfaces.ClickListners.Updateable
import com.app.purchase.Prefrences.CurrencyPrefs
import com.app.purchase.R
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities
import com.app.purchase.ViewModel.CreditDebit.CreditDebitViewModel
import com.app.purchase.Views.adapters.CreditDebitAdapter
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class Debit : Fragment(), View.OnClickListener, DebitCreditSaveClick, OnDebitCreditItemClick , Updateable{

    override fun update() {
        if (creditDebitAdapter != null) {
            creditDebitAdapter!!.notifyDataSetChanged()
        }
    }
    private var DateAndTime = ""

    var rootview: View? = null
    private var debitRecyclerView: RecyclerView? = null
    private var debbitdetailButton: ImageView? = null
    private var list: List<CreditDebitEntities>? = null
    private var creditDebitAdapter: CreditDebitAdapter? = null
    private var debitViewModel: CreditDebitViewModel? = null
    private var sharedPreferences : SharedPreferences?  = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_debit, container, false)
        sharedPreferences =  context?.getSharedPreferences("APP_CURRENCY_SETTINGS",Context.MODE_PRIVATE)!!
        initViews(rootview)
        setUpRecyclerView()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                IntentFilter("custom-currency-even"))
        return rootview
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("message")
            if (message == "change currency") {
                creditDebitAdapter?.notifyDataSetChanged()
            }
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
    override fun onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }


    private fun initViews(rootview: View?) {
        list = ArrayList()
        debitRecyclerView = rootview?.findViewById(R.id.debitRecyclerView)
        debbitdetailButton = rootview?.findViewById(R.id.debbitdetailButton)
        debbitdetailButton?.setOnClickListener(this)
    }

    private fun setUpRecyclerView() {

        creditDebitAdapter = list?.let { CreditDebitAdapter(context!!,"", it, this) }
        debitRecyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        debitRecyclerView!!.adapter = creditDebitAdapter

        debitViewModel = ViewModelProviders.of(this@Debit).get(CreditDebitViewModel::class.java)
        debitViewModel?.allCreditDebit?.observe(this, Observer {
            creditDebitAdapter?.addCreditSale(it!!)
        })
    }


    private fun addFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, fragment)?.addToBackStack(null)?.commit()
    }

    companion object {
        @JvmStatic
        fun pasObjInArgs(fragment: Fragment, debitEntities: CreditDebitEntities) = fragment.apply {
            arguments = Bundle().apply {
                putParcelable("debitEntities", debitEntities)
            }
        }
    }

    override fun onClick(p0: View?) {
        val id: Int = p0!!.id
        when (id) {
            R.id.debbitdetailButton -> addFragment(DebitDetailFragment(this))
        }

    }

    override fun OnDabitCreditSaveClick(creditDebitEntities: CreditDebitEntities) {
        debitViewModel?.insert(creditDebitEntities)
    }

    override fun OnItemClick(debitEntities: CreditDebitEntities) {
        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, pasObjInArgs(DebitDetailFragment(this), debitEntities))?.addToBackStack(null)?.commit()
    }


}
