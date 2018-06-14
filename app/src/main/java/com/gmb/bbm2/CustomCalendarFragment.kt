package com.gmb.bbm2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.dummy.DummyContent.DummyItem
import com.gmb.bbm2.tools.adapter.GridCellAdapter
import com.gmb.bbm2.tools.adapter.NotifAdapter
import com.gmb.bbm2.tools.allstatic.*
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.json.MyJsonParserKotlin
import com.gmb.gmbdatepickerpro.DatePickerPro
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_custom_calendar_list.view.*
import kotlinx.android.synthetic.main.lyt_head_calendar.view.*
import kotlinx.android.synthetic.main.notif_list.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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
class CustomCalendarFragment : Fragment(),NotifAdapter.OnItemSelectListener,View.OnClickListener {




    override fun onClick(p0: View?) {


        if (p0 === rootView!!.imgPrevious) {

            if (monthMode) {

                dateCour = getFirstDayPreviousMonth(date1)

                d1 = dateCour
                d2 = getLastDayMonth(d1)
                rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))

            } else {

                val cale = GregorianCalendar()
                cale.timeInMillis = dateCour
                cale.add(Calendar.DAY_OF_MONTH, -7)
                dateCour = getFirstDayWeek(cale.timeInMillis)

                d1 = dateCour
                d2 = getLastDayWeek(d1)
                rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))
            }

            date1=d1
            date2=d2

            //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "        + month + " Year: " + year);
            setGridCellAdapterToDate(dateCour)
            updateView()

        }

        if (p0 === rootView!!.imgNext) {

            if (monthMode) {

                dateCour = getFirstDayNextMonth(date1)

                d1 = dateCour
                d2 = getLastDayMonth(d1)
                rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))

            } else {

                val cale = GregorianCalendar()
                cale.timeInMillis = dateCour
                cale.add(Calendar.DAY_OF_MONTH, 7)
                dateCour =getFirstDayWeek(cale.timeInMillis)

                d1 = dateCour
                d2 = getLastDayWeek(d1)
                rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))
            }


            date1=d1
            date2=d2
            //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "        + month + " Year: " + year);
            setGridCellAdapterToDate(dateCour)
            updateView()
        }


        if (p0 === rootView!!.txtMonth) {

            popUpDateDialog()
        }
    }


    private val datePickedListener = DatePickerPro.OnDatePickedListener { datePicked ->
        dateCour = datePicked
        if (monthMode) {


            d1 = getFirstDayMonth(dateCour)
            d2 = getLastDayMonth(dateCour)
            rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))

        } else {

            d1 = getFirstDayWeek(dateCour)
            d2 = getLastDayWeek(dateCour)
            rootView!!.txtMonth.setText(datFor.format(Date(dateCour)))
        }

        //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "        + month + " Year: " + year);
        setGridCellAdapterToDate(dateCour)

    }


    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    private var rootView:View?=null
    private var monthMode=true
    private var dateCour=System.currentTimeMillis()
    private var d1=dateCour
    private var d2=dateCour
    private var datFor=SimpleDateFormat("MMM. yyyy")

    internal lateinit var fsEdit: MyFirestoreEditor
    internal lateinit var mQuery: Query
    internal lateinit var mAdapter: NotifAdapter
    internal lateinit var catList: ArrayList<OnTimeNotificationFB>

    private var gridCalendarView: GridView? = null
    private var viewBelowGrid: View? = null
    private var adapter: MyGridCellAdapter? = null
    private var cal: GregorianCalendar? = GregorianCalendar()
    private var date1:Long=System.currentTimeMillis()
    private var date2:Long=System.currentTimeMillis()

    private lateinit var myRecycle:RecyclerView


    override fun onItemSelected(documentSnap: DocumentSnapshot, mTwoPane: Boolean) {


        val buf = documentSnap.toObject(OnTimeNotificationFB::class.java)
        //Resources resources = itemView.getResources();

        val js = JSONObject(MyJsonParserKotlin.onTimeNotifToJsonString(buf))

        /*val item = v.tag as DummyContent.DummyItem*/
        if (mTwoPane) {
            /*val fragment = NotifDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(NotifDetailFragment.ARG_OBJ_AS_JSON, js.toString())
                }
            }*/
            val fragment=NotifDetailFragment.newInstance(js.toString(),js.getString("sens"),js.getLong("datestart").toString(),
                    js.getString("idCat"),js.getString("idStat"),System.currentTimeMillis())
            this.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notif_detail_container, fragment)
                    .commit()
        } else {

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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }

        date1= getFirstDayMonth(date1)
        date2= getLastDayMonth(date1)
        cal=GregorianCalendar()
        cal!!.timeInMillis=date1
    }













    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         rootView = inflater!!.inflate(R.layout.fragment_custom_calendar_list, container, false)


        rootView!!.cmdCustomNew.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()


                val intent = Intent(this.activity, NotifDetailActivity::class.java).apply {
                    putExtra(NotifDetailFragment.ARG_OBJ_AS_JSON, "")
                    putExtra(NotifDetailFragment.ARG_SENS,getString(R.string.sens_debit))
                    putExtra(NotifDetailFragment.ARG_DATE_COUR,System.currentTimeMillis())
                    putExtra(NotifDetailFragment.ARG_DATE_START_PARAM,System.currentTimeMillis().toString())



                }
                startActivity(intent)


        }

        gridCalendarView=rootView!!.lyt_grdCalendar

        // Set the adapter
        /*if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = MyCustomCalendarViewAdapter(DummyContent.ITEMS, mListener)
        }*/
        adapter = MyGridCellAdapter(activity,this, System.currentTimeMillis(),true)
        rootView!!.lyt_grdCalendar.setAdapter(adapter)

        myRecycle=rootView!!.notif_list

        fsEdit = getFireStoreEditor(this.context)

        rootView!!.txtMonth.setText(datFor.format(Date(date1)))

        rootView!!.imgNext.setOnClickListener (this)
        rootView!!.imgPrevious.setOnClickListener(this)
        rootView!!.txtMonth.setOnClickListener(this)

        /*





// Get ${LIMIT} restaurants
mQuery = mFirestore.collection("restaurants")
        .orderBy("avgRating", Query.Direction.DESCENDING)
        .limit(LIMIT);*/

        mQuery = fsEdit.getQueryFromCollection(MyFirestoreEditor.COLLECTION_ONTIMENOTIF, 100).whereGreaterThanOrEqualTo(COL_NAME_DATESTART,date1)
                .whereLessThanOrEqualTo(COL_NAME_DATESTART,date2)
                .orderBy(COL_NAME_DATESTART)




        mAdapter =  NotifAdapter(mQuery, this,false,rootView!!.notif_list,this.activity as AppCompatActivity)


//recList.setHasFixedSize(true);
        val llm = LinearLayoutManager(this.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rootView!!.notif_list.setLayoutManager(llm)

//setListAdapter(mAdapter);
        rootView!!.notif_list.setAdapter(mAdapter)

       //updateView()

        return rootView
    }


    fun setGridCellAdapterToDate(timeParam: Long) {

        cal!!.setTimeInMillis(timeParam)

        adapter = MyGridCellAdapter(activity,this, timeParam,monthMode)
        rootView!!.txtMonth.setText(datFor.format(cal!!.getTime()))
        adapter!!.notifyDataSetChanged()
        gridCalendarView!!.setAdapter(adapter)
        updateView()
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



    public fun updateView(){

        date1= getFirstHourDay(date1)
        date2= getLastHourDay(date2)

        mQuery = fsEdit.getQueryFromCollection(MyFirestoreEditor.COLLECTION_ONTIMENOTIF, 100).whereGreaterThanOrEqualTo(COL_NAME_DATESTART,date1)
                .whereLessThanOrEqualTo(COL_NAME_DATESTART,date2)
                .orderBy(COL_NAME_DATESTART)




        updateViewFromQuery(mQuery)
    }



    public fun updateViewFromQuery(q: Query){



        mAdapter.resetQuery(q)

        /*mAdapter =  NotifAdapter(q, this,false,myRecycle,this.activity as AppCompatActivity)

        rootView!!.txtMonth.setText(datFor.format(Date(date1)))

//recList.setHasFixedSize(true);
        val llm = LinearLayoutManager(this.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        myRecycle.setLayoutManager(llm)*/

//setListAdapter(mAdapter);
        mAdapter.notifyDataSetChanged()
        //myRecycle.setAdapter(mAdapter)
        //myRecycle.Recycler()


    }


    private fun popUpDateDialog() {

        val dialog = DatePickerPro(activity, datePickedListener, dateCour, true)
        dialog.show()
    }


    class MyGridCellAdapter( private val context: Context?,private val custom:CustomCalendarFragment,
                             private val dat:Long,
                             private val isMonthMode:Boolean):GridCellAdapter(context,dat,isMonthMode){


        /*class MyGridCellAdapter(private val context: Context?,
                            private val dat:Long,
                            private val isMonthMode:Boolean):GridCellAdapter(context,dat,isMonthMode){*/



        /*constructor(  context: Context?,
                      custom:CustomCalendarFragment,
                      dat:Long,
                      isMonthMode:Boolean,
                      otherBol:Boolean) : this(context,custom,dat,isMonthMode){


        }*/



        override fun updateDayChooseEvents(d1: Long, d2: Long) {

            custom.date1=d1
            custom.date2=d2

            custom.updateView()
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
        fun newInstance(columnCount: Int): CustomCalendarFragment {
            val fragment = CustomCalendarFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
