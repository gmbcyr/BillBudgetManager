package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.tools.adapter.NotifAdapter
import com.gmb.bbm2.tools.allstatic.getFireStoreEditor
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.json.MyJsonParserKotlin
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_notif_list.*
import kotlinx.android.synthetic.main.notif_list.*
import org.json.JSONObject

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [NotifDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class NotifListActivity : AppCompatActivity(), NotifAdapter.OnItemSelectListener,CategoryFragment.OnCategoryFragmentListener {
    override fun newCatCreated(id: String, name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




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
            this.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notif_detail_container, fragment)
                    .commit()
        } else {

            val intent = Intent(this.applicationContext, NotifDetailActivity::class.java).apply {
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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private var mTwoPane: Boolean = false

    internal lateinit var fsEdit: MyFirestoreEditor
    internal lateinit var mQuery: Query
    internal lateinit var mAdapter: NotifAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif_list)

        setSupportActionBar(toolbarNotif)
        toolbarNotif.title = title

        fabNotif.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            //if (mTwoPane) {
            if (1>2) {



                val fragment = NotifDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(NotifDetailFragment.ARG_OBJ_AS_JSON, "")
                    }
                }
                this.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.category_detail_container, fragment)
                        .commit()
            } else {
                val intent = Intent(this, NotifDetailActivity::class.java).apply {
                    putExtra(NotifDetailFragment.ARG_OBJ_AS_JSON, "")
                    putExtra(NotifDetailFragment.ARG_SENS,getString(R.string.sens_debit))
                    putExtra(NotifDetailFragment.ARG_DATE_COUR,System.currentTimeMillis())
                    putExtra(NotifDetailFragment.ARG_DATE_START_PARAM,System.currentTimeMillis().toString())



                }
                startActivity(intent)
            }
        }

        /*if (notif_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }*/



        fsEdit = getFireStoreEditor(this)
// Get ${LIMIT} restaurants
/*mQuery = mFirestore.collection("restaurants")
        .orderBy("avgRating", Query.Direction.DESCENDING)
        .limit(LIMIT);*/

        mQuery = fsEdit.getQueryFromCollection(MyFirestoreEditor.COLLECTION_ONTIMENOTIF, 100)

        val viewFinal = this
// RecyclerView
//mAdapter =  CategoryAdapterOld(mQuery, this, mTwoPane)


        mAdapter =  NotifAdapter(mQuery, this,false,notif_list,this )


//recList.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        notif_list.setLayoutManager(llm)

//setListAdapter(mAdapter);
        notif_list.setAdapter(mAdapter)



    }


    public override fun onStart() {
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

    public override fun onStop() {
        super.onStop()
        if (mAdapter != null) {
            mAdapter.stopListening()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        //recyclerView.adapter = MyRecyclerViewAdapter(this, catList, mTwoPane)
        //recyclerView.adapter = mAdapter
    }



}
