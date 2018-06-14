package com.gmb.bbm2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.dummy.DummyContent.DummyItem
import com.gmb.bbm2.tools.adapter.NotifAdapter
import com.gmb.bbm2.tools.allstatic.getFireStoreEditor
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.json.MyJsonParserKotlin
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_notif_list.view.*
import org.json.JSONObject

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
class NotifListFragment : Fragment(), NotifAdapter.OnItemSelectListener {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    internal lateinit var fsEdit: MyFirestoreEditor
    internal lateinit var mQuery: Query
    internal lateinit var mAdapter: NotifAdapter


    override fun onItemSelected(documentSnap: DocumentSnapshot, mTwoPane: Boolean) {


        val buf = documentSnap.toObject(OnTimeNotificationFB::class.java)
        //Resources resources = itemView.getResources();

        val js = JSONObject(MyJsonParserKotlin.onTimeNotifToJsonString(buf))

        /*val item = v.tag as DummyContent.DummyItem*/

            val intent = Intent(this.activity.applicationContext, NotifDetailActivity::class.java).apply {
                putExtra(NotifDetailFragment.ARG_OBJ_AS_JSON, js.toString())
                putExtra(NotifDetailFragment.ARG_SENS,js.getString("sens"))
                putExtra(NotifDetailFragment.ARG_DATE_START_PARAM,js.getLong("datestart").toString())
                putExtra(NotifDetailFragment.ARG_ID_CAT,js.getString("idcat"))
                putExtra(NotifDetailFragment.ARG_ID_STAT,js.getString("idstate"))
                putExtra(NotifDetailFragment.ARG_ITEM_ID,js.getString("id"))
            }
            Log.e("NotifList", "this is what a send ->"+js.toString())
            startActivity(intent)

            /*val intent = Intent(this.applicationContext, CategoryDetailActivity::class.java).apply {
                putExtra(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
            }
            startActivity(intent)*/

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_notif_list, container, false)

        // Set the adapter
        /*if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = MyNotifListAdapter(DummyContent.ITEMS, mListener)
        }*/

        fsEdit = getFireStoreEditor(this.context)
// Get ${LIMIT} restaurants
/*mQuery = mFirestore.collection("restaurants")
        .orderBy("avgRating", Query.Direction.DESCENDING)
        .limit(LIMIT);*/

        mQuery = fsEdit.getQueryFromCollection(MyFirestoreEditor.COLLECTION_ONTIMENOTIF, 100)




        mAdapter =  NotifAdapter(mQuery, this,false,view.rcv_notif_list_in_frag,this.activity as AppCompatActivity)


//recList.setHasFixedSize(true);
        val llm = LinearLayoutManager(this.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        view.rcv_notif_list_in_frag.setLayoutManager(llm)

//setListAdapter(mAdapter);
        view.rcv_notif_list_in_frag.setAdapter(mAdapter)

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

     override fun onStart() {
        super.onStart()

        // Start sign in if necessary
        /*if (shouldStartSignIn()) {
            startSignIn()
            return
        }*/

        // Apply filters
        //onFilter(mViewModel.getFilters())

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening()
        }
    }

     override fun onStop() {
        super.onStop()
        if (mAdapter != null) {
            mAdapter.stopListening()
        }
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
        fun newInstance(columnCount: Int): NotifListFragment {
            val fragment = NotifListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
