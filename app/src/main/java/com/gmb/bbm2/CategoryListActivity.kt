package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gmb.bbm2.data.model.CategoryFB
import com.gmb.bbm2.dummy.DummyContent
import com.gmb.bbm2.tools.adapter.CategoryAdapter
import com.gmb.bbm2.tools.adapter.oldversion.CategoryAdapterOld
import com.gmb.bbm2.tools.allstatic.getFireStoreEditor
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.firestore.onActionDone
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_category_list.*
import kotlinx.android.synthetic.main.category_list.*
import kotlinx.android.synthetic.main.category_list_content.view.*
import org.json.JSONObject
import java.util.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [CategoryDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class CategoryListActivity : AppCompatActivity(), CategoryAdapter.OnItemSelectListener,CategoryFragment.OnCategoryFragmentListener {
    override fun newCatCreated(id: String, name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onItemSelected(documentSnap: DocumentSnapshot, mTwoPane: Boolean) {


        val buf = documentSnap.toObject(CategoryFB::class.java)
        //Resources resources = itemView.getResources();

        val js=JSONObject()
        js.put("nom",buf.nom)
        js.put("montant",buf.minamount)
        js.put("nomAff",buf.nomaffiche);
        js.put("overdue",buf.allowoverdue)
        js.put("id",buf.id)

        /*val item = v.tag as DummyContent.DummyItem*/
        if (mTwoPane) {
            val fragment = CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
                }
            }
            this.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.category_detail_container, fragment)
                    .commit()
        } else {

            val intent = Intent(this.applicationContext, CategoryDetailActivity::class.java).apply {
                putExtra(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
            }
            Log.e("CategoryList", "this is what a send ->"+js.toString())
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
private var mTwoPane: Boolean = false

internal lateinit var fsEdit: MyFirestoreEditor
internal lateinit var mQuery: Query
internal lateinit var mAdapter: CategoryAdapterOld
    internal lateinit var mAdapterK: CategoryAdapter
    internal lateinit var catList:ArrayList<CategoryFB>


override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_category_list)

setSupportActionBar(toolbar)
toolbar.title = title

fab.setOnClickListener { view ->
    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    if (mTwoPane) {
        val fragment = CategoryFragment().apply {
            arguments = Bundle().apply {
                putString(CategoryFragment.ARG_OBJ_AS_JSON, "")
            }
        }
        this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.category_detail_container, fragment)
                .commit()
    } else {
        val intent = Intent(this, CategoryDetailActivity::class.java).apply {
            putExtra(CategoryDetailFragment.ARG_ITEM_ID, "")
        }
        startActivity(intent)
    }
}

if (category_detail_container != null) {
    // The detail container view will be present only in the
    // large-screen layouts (res/values-w900dp).
    // If this view is present, then the
    // activity should be in two-pane mode.
    mTwoPane = true
}



fsEdit = getFireStoreEditor(this)
// Get ${LIMIT} restaurants
/*mQuery = mFirestore.collection("restaurants")
        .orderBy("avgRating", Query.Direction.DESCENDING)
        .limit(LIMIT);*/

mQuery = fsEdit.getQueryFromCollection(MyFirestoreEditor.COLLECTION_CATEGORY, 100)

val viewFinal = this
// RecyclerView
//mAdapter =  CategoryAdapterOld(mQuery, this, mTwoPane)


    mAdapterK =  CategoryAdapter(mQuery, this,mTwoPane,category_list,category_list,category_list)


//recList.setHasFixedSize(true);
val llm = LinearLayoutManager(this)
llm.orientation = LinearLayoutManager.VERTICAL
category_list.setLayoutManager(llm)

//setListAdapter(mAdapter);
category_list.setAdapter(mAdapterK)

    class onAction(simple:String):onActionDone{

        override fun onListDone(list: QuerySnapshot?) {

            catList = ArrayList<CategoryFB>()
            if (list != null) {

                catList = list.toObjects<CategoryFB>(CategoryFB::class.java) as ArrayList<CategoryFB>


                Log.e("CategoryListAct", "this is category list from firestore->" + list!!.size())


                //setupRecyclerView(category_list)

            }
        }


    }

    //fsEdit.getListItems(MyFirestoreEditor.COLLECTION_CATEGORY,  onAction("val"))
    //fsEdit.getListItems(MyFirestoreEditor.COLLECTION_CATEGORY,  onAction("val"))


//setupRecyclerView(category_list)
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
        if (mAdapterK != null) {
            mAdapterK.startListening()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (mAdapterK != null) {
            mAdapterK.stopListening()
        }
    }

private fun setupRecyclerView(recyclerView: RecyclerView) {
recyclerView.adapter = MyRecyclerViewAdapter(this, catList, mTwoPane)
    //recyclerView.adapter = mAdapter
}


    class MyRecyclerViewAdapter(private val mParentActivity: CategoryListActivity,
                                           private val mValues: List<CategoryFB>,
                                           private val mTwoPane: Boolean) :
            RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                /*val item = v.tag as CategoryFB
                if (mTwoPane) {
                    val fragment = CategoryDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(CategoryDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.category_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, CategoryDetailActivity::class.java).apply {
                        putExtra(CategoryDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }*/



                val buf = v.tag as CategoryFB

                //val buf = documentSnap.toObject(CategoryFB::class.java)
                //Resources resources = itemView.getResources();

                val js=JSONObject()
                js.put("nom",buf.nom)
                js.put("montant",buf.minamount)
                js.put("nomAff",buf.nomaffiche);
                js.put("overdue",buf.allowoverdue)
                js.put("id",buf.id)

                /*val item = v.tag as DummyContent.DummyItem*/
                if (mTwoPane) {
                    val fragment = CategoryFragment().apply {
                        arguments = Bundle().apply {
                            putString(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.category_detail_container, fragment)
                            .commit()
                } else {

                    val intent = Intent(v.context, CategoryDetailActivity::class.java).apply {
                        putExtra(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
                    }
                    Log.e("CategoryList", "this is what a send ->"+js.toString())
                    v.context.startActivity(intent)



                    /*val intent = Intent(this.applicationContext, CategoryDetailActivity::class.java).apply {
                        putExtra(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
                    }
                    startActivity(intent)*/
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_list_content, parent, false)

            Log.e("MyRecyclerViewAdapter", "this is OnCreateViewHolder method for ->")
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.id
            holder.mContentView.text = item.nom

            Log.e("MyRecyclerViewAdapter", "this is bind method for ->" + item.nom)
            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }








    class SimpleItemRecyclerViewAdapterOld(private val mParentActivity: CategoryListActivity,
                                        private val mValues: List<DummyContent.DummyItem>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapterOld.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (mTwoPane) {
                    val fragment = CategoryDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(CategoryDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.category_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, CategoryDetailActivity::class.java).apply {
                        putExtra(CategoryDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.id
            holder.mContentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
