package com.gmb.bbm2.tools.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmb.bbm2.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OptionFilterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OptionFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionFilterFragment : DialogFragment() {

    val TAG = "FilterDialog"

    internal interface FilterListener {

        fun onFilter(filters: Filters)

    }

    private var mRootView: View? = null



    private var mFilterListener: FilterListener? = null



    companion object {

        val TAG = "SeekDialogFragment::class.qualifiedName"
        val ARG_VALUE = "value"
        val ARG_MIN = "min"
        val ARG_MAX = "max"



        fun showMyDialog(activity:Activity,option:String){


            val aff=OptionFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VALUE, option)
                    //putInt(ARG_MIN, range.first)
                    //putInt(ARG_MAX, range.last)
                }
            }

            //aff.show(activity!!.fragmentManager, TAG)

        }
    }


    public fun showMyDialog2(activity:Activity,option:String){


        val aff=OptionFilterFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_VALUE, option)
                //putInt(ARG_MIN, range.first)
                //putInt(ARG_MAX, range.last)
            }
        }

        this.show(this.activity.supportFragmentManager, Companion.TAG)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            /*argTitle = arguments.getString(ARG_TITLE)
            argClass = arguments.getString(ARG_CLASS)
            argTypeListing = arguments.getString(ARG_TYPE_LISTING)
            argCat = arguments.getString(ARG_CAT)
            argSens = arguments.getString(ARG_SENS)
            argStatus = arguments.getString(ARG_STATUS)
            argDate1 = arguments.getString(ARG_DATE1)
            argDate2 = arguments.getString(ARG_DATE2)*/
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mRootView = inflater!!.inflate(R.layout.fragment_option_filter, container, false)

        return mRootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilterListener) {
            mFilterListener = context
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //@OnClick(R.id.button_search)
    fun onSearchClicked() {
        if (mFilterListener != null) {
            //mFilterListener!!.onFilter(getFilters())
        }

        dismiss()
    }

    //@OnClick(R.id.button_cancel)
    fun onCancelClicked() {
        dismiss()
    }

    /*private fun getSelectedCategory(): String? {
        val selected = mCategorySpinner!!.selectedItem as String
        return if (getString(R.string.value_any_category) == selected) {
            null
        } else {
            selected
        }
    }

    private fun getSelectedCity(): String? {
        val selected = mCitySpinner!!.selectedItem as String
        return if (getString(R.string.value_any_city) == selected) {
            null
        } else {
            selected
        }
    }

    private fun getSelectedPrice(): Int {
        val selected = mPriceSpinner!!.selectedItem as String
        return if (selected == getString(R.string.price_1)) {
            1
        } else if (selected == getString(R.string.price_2)) {
            2
        } else if (selected == getString(R.string.price_3)) {
            3
        } else {
            -1
        }
    }

    private fun getSelectedSortBy(): String? {
        val selected = mSortSpinner!!.selectedItem as String
        if (getString(R.string.sort_by_rating) == selected) {
            return Restaurant.FIELD_AVG_RATING
        }
        if (getString(R.string.sort_by_price) == selected) {
            return Restaurant.FIELD_PRICE
        }
        return if (getString(R.string.sort_by_popularity) == selected) {
            Restaurant.FIELD_POPULARITY
        } else null

    }

    private fun getSortDirection(): Query.Direction? {
        val selected = mSortSpinner!!.selectedItem as String
        if (getString(R.string.sort_by_rating) == selected) {
            return Query.Direction.DESCENDING
        }
        if (getString(R.string.sort_by_price) == selected) {
            return Query.Direction.ASCENDING
        }
        return if (getString(R.string.sort_by_popularity) == selected) {
            Query.Direction.DESCENDING
        } else null

    }

    fun resetFilters() {
        if (mRootView != null) {
            mCategorySpinner!!.setSelection(0)
            mCitySpinner!!.setSelection(0)
            mPriceSpinner!!.setSelection(0)
            mSortSpinner!!.setSelection(0)
        }
    }

    fun getFilters(): Filters {
        val filters = Filters()

        if (mRootView != null) {
            filters.category = getSelectedCategory()
            filters.city = getSelectedCity()
            filters.price = getSelectedPrice()
            filters.sortBy = getSelectedSortBy()
            filters.sortDirection = getSortDirection()
        }

        return filters
    }*/
}
