/*
package com.choraline.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.instalets.HomeActivity
import com.instalets.R
import com.instalets.utils.Constants
import com.instalets.utils.Utility


class HomeFragment : Fragment(), View.OnClickListener {


    */
/*private var context: Context? = null
    private var txtTitleBack: TextView? = null
    private var btnRentProperty: Button? = null
    private var btnRentMultipleRooms: Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context = activity
        initUI()

    }

    override fun onResume() {
        super.onResume()
        Utility.isUserInLoggedInSection = false
    }

    private fun initUI() {
        txtTitleBack = (activity as HomeActivity).findViewById(R.id.toolbar_txtTitle) as TextView
        txtTitleBack!!.text = "Home"
        txtTitleBack!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        txtTitleBack!!.compoundDrawablePadding = 5
        txtTitleBack!!.setOnClickListener(null)

        btnRentProperty = view!!.findViewById(R.id.home_btnRentProperty) as Button
        btnRentMultipleRooms = view!!.findViewById(R.id.home_btnRentMultipleRoom) as Button


        btnRentProperty!!.setOnClickListener(this)
        btnRentMultipleRooms!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {

        if (v === btnRentProperty) {
            val fragment = RentPropertyFragment()
            val b = Bundle()
            b.putString(Constants.LET_PROPERTY_TYPE, Constants.LET_PROPERTY)
            fragment.setArguments(b)
            (activity as HomeActivity).addFragment(fragment)
        }
        if (v === btnRentMultipleRooms) {
            val fragment = RentPropertyFragment()
            val b = Bundle()
            b.putString(Constants.LET_PROPERTY_TYPE, Constants.LET_MULTIPLE_ROOMS)
            fragment.setArguments(b)
            (activity as HomeActivity).addFragment(fragment)
        }
    }
*//*

}


*/
