package com.app.purchase

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.app.purchase.ViewModel.CurrencyViewModel
import com.app.purchase.Views.Tabs.Debit.Debit
import com.app.purchase.Views.Tabs.Sales.Sale
import com.app.purchase.Views.Tabs.SettingsFragment
import com.app.purchase.Views.adapters.PagerAdapter
import com.app.purchase.Views.stats.Stats


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewpager: ViewPager
    private lateinit var adpter: PagerAdapter
    private lateinit var stats: LinearLayout
    private lateinit var opensetting: TextView
    private lateinit var purchase: TextView
    private lateinit var statsText: TextView
    private lateinit var settinglayout: LinearLayout
    private lateinit var debit: TextView
    private lateinit var sale: TextView
    private val selectedTabColor: Int = R.color.selectedTabColor
    private val whiteColor: Int = R.color.white
    private lateinit var currencyViewModel: CurrencyViewModel
    var galleryPermission = 0
    private val REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setupPermissions()
        initViews()
    }

    private fun initViews() {
        settinglayout = findViewById(R.id.settinglayout)
        settinglayout.setOnClickListener(this)
        viewpager = findViewById(R.id.viewPager)
        sale = findViewById(R.id.Sale)
        sale.setOnClickListener(this)
        purchase = findViewById(R.id.Purchase)
        purchase.setOnClickListener(this)
        debit = findViewById(R.id.Debit)
        debit.setOnClickListener(this)
        stats = findViewById(R.id.Stats)
        stats.setOnClickListener(this)
        statsText = findViewById(R.id.StatsText)
        currencyViewModel = ViewModelProviders.of(this@MainActivity).get(CurrencyViewModel::class.java)


        ChangeLabelsColor(selectedTabColor, whiteColor, whiteColor, whiteColor)
        setPagerAdapter()
    }

    fun setPagerAdapter() {
        adpter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adpter
        adpter.addFragment(Sale("Sale"))
        adpter.addFragment(Sale("Purchase"))
        adpter.addFragment(Debit())
        adpter.addFragment(Stats())
        adpter.notifyDataSetChanged()
        PagerListners()
    }

    private fun opensetting(fragment: Fragment) {
        this.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)?.replace(R.id.MainContainer, fragment)?.addToBackStack(null)?.commit()
    }

    private fun PagerListners() {
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                changeTitlesColor(position)
            }
        })
    }

    private fun changeTitlesColor(position: Int) {
        when (position) {
            0 -> ChangeLabelsColor(selectedTabColor, whiteColor, whiteColor, whiteColor)
            1 -> ChangeLabelsColor(whiteColor, selectedTabColor, whiteColor, whiteColor)
            2 -> ChangeLabelsColor(whiteColor, whiteColor, selectedTabColor, whiteColor)
            3 -> ChangeLabelsColor(whiteColor, whiteColor, whiteColor, selectedTabColor)
        }
    }

    private fun ChangeLabelsColor(salesColor: Int, purchasColor: Int, debitColor: Int, statsColor: Int) {
        purchase.setTextColor(resources.getInteger(purchasColor))
        debit.setTextColor(resources.getInteger(debitColor))
        sale.setTextColor(resources.getInteger(salesColor))
        statsText.setTextColor(resources.getInteger(statsColor))
    }

    override fun onClick(p0: View?) {
        val id: Int = p0!!.id
        when (id) {
            R.id.settinglayout -> opensetting(SettingsFragment())
            R.id.Sale -> {
                ChangeLabelsColor(selectedTabColor, whiteColor, whiteColor, whiteColor)
                viewpager.currentItem = 0
            }
            R.id.Purchase -> {
                ChangeLabelsColor(whiteColor, selectedTabColor, whiteColor, whiteColor)
                viewpager.currentItem = 1
            }
            R.id.Debit -> {
                ChangeLabelsColor(whiteColor, whiteColor, selectedTabColor, whiteColor)
                viewpager.currentItem = 2
            }
            R.id.Stats -> {
                ChangeLabelsColor(whiteColor, whiteColor, whiteColor, selectedTabColor)
                viewpager.currentItem = 3
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
//        if (supportFragmentManager.backStackEntryCount == 0) {
//            viewpager.adapter?.notifyDataSetChanged()
//        }
    }

    companion object {
        var currency = ""
    }

    private fun setupPermissions() {
        galleryPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        val camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)


        val contact = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
        if (galleryPermission != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED
        || contact != PackageManager.PERMISSION_GRANTED ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS), REQUEST_CODE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            galleryPermission -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

}
