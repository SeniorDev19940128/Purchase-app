package com.app.purchase.Views.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.purchase.Views.Tabs.Debit.Debit
import com.app.purchase.Views.Tabs.Sales.Sale


/**
 * Created by Android on 12/6/2018.
 */
class PagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }


//    override fun getItemPosition(`object`: Any): Int {
//        if (fragmentList[0] == Sale("Sale")) {
//            val sale = `object` as Sale?
//            if (sale != null) {
//                sale!!.update()
//            }
//        }
//        if (fragmentList[1] == Sale("Purchase")) {
//            val sale = `object` as Sale?
//            if (sale != null) {
//                sale!!.update()
//            }
//        }
//        if (fragmentList[2] == Debit()) {
//            val debit = `object` as Debit?
//            if (debit != null) {
//                debit.update()
//            }
//        }
////        return PagerAdapter.POSITION_NONE
//    }

}