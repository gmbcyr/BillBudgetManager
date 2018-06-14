package com.gmb.bbm2

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gmb.bbm2.dummy.DummyContent
import com.gmb.bbm2.dummy.DummyContent.DummyItem
import com.gmb.bbm2.tools.adapter.oldversion.NotifAdapterOld
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.viewmodel.ListingViewModel
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class AllListingFragment : Fragment(), NotifAdapterOld.OnNotifSelectedListener {
    override fun onNotifSelected(restaurant: DocumentSnapshot?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TAG = "AllListing"

    private val RC_SIGN_IN = 9001

    private val LIMIT = 60

    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null

    //private var mFilterDialog: FilterDialogFragment? = null
    private var mAdapter: NotifAdapterOld? = null

    private var mViewModel: ListingViewModel? = null


    internal lateinit  var recyView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }





    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
             recyView = view as RecyclerView
            if (mColumnCount <= 1) {
                recyView.layoutManager = LinearLayoutManager(context)
            } else {
                recyView.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            recyView.adapter = MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener)
        }


        // View model
        mViewModel = ViewModelProviders.of(this).get(ListingViewModel::class.java!!)

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true)

        // Firestore
        mFirestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        mFirestore!!.setFirestoreSettings(settings)


        val fbEdit:MyFirestoreEditor?= MyFirestoreEditor(view.getContext())

        // Get ${LIMIT} restaurants
        /*mQuery = mFirestore!!.collection("restaurants")
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT.toLong())*/

        mQuery = fbEdit!!.getCollectionRef(MyFirestoreEditor.COLLECTION_ONTIMENOTIF)
                .orderBy("timestart", Query.Direction.DESCENDING)
                .limit(LIMIT.toLong())

        // RecyclerView
        mAdapter = object : NotifAdapterOld(mQuery, this) {
            protected override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() === 0) {
                    /*recyView.setVisibility(View.GONE)
                    mEmptyView.setVisibility(View.VISIBLE)*/
                } else {
                    recyView.setVisibility(View.VISIBLE)
                    //mEmptyView.setVisibility(View.GONE)
                }
            }

            protected override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
               /* Snackbar.make(findViewById<View>(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()*/

                Snackbar.make(view.section_label.content,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }

        recyView.setAdapter(mAdapter)



        return view
    }





    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): AllListingFragment {
            val fragment = AllListingFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
