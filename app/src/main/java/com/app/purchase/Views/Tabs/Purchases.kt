package com.app.purchase.Views.Tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.purchase.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Purchases.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Purchases.newInstance] factory method to
 * create an instance of this fragment.
 */
class Purchases : Fragment() {
    private var rootview: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_purchases, container, false)
        return rootview
    }


}// Required empty public constructor
