package com.gmb.bbm2.tools.adapter

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gmb.bbm2.R
import com.gmb.bbm2.data.model.CategoryFB
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

/**
 * Created by GMB on 3/12/2018.
 */
class CategoryAdapter(private val query: Query,
                      private val mListener: OnItemSelectListener,
                      private val mTwoPane: Boolean,
                      private val viewRoot:View,
                      private val recyclerView: RecyclerView,
                      private val emptyView:View) :
        MyFBadapter<CategoryAdapter.ViewHolder>(query) {


    override   fun onDataChanged() {
        // Show/hide content if the query returns empty.
        /*if (getItemCount() === 0) {
            recyclerView.setVisibility(View.GONE)
            emptyView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.setVisibility(View.VISIBLE)
            emptyView.setVisibility(View.GONE)
        }*/
    }

    override   fun onError(e: FirebaseFirestoreException) {
        // Show a snackbar on errors
        Snackbar.make(viewRoot,"Error: check logs for info.", Snackbar.LENGTH_LONG).show()
    }

    interface OnItemSelectListener {

        fun onItemSelected(document: DocumentSnapshot, twoPane:Boolean)

    }

    //private lateinit var  mListener: OnItemSelectListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= ViewHolder(inflater.inflate(R.layout.category_detail, parent, false))



        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), mListener,mTwoPane)

        var buf: CategoryFB = getSnapshot(position)!!.toObject(CategoryFB::class.java)


    }

    class ViewHolder(itemView: View)//ButterKnife.bind(this, itemView);
        : RecyclerView.ViewHolder(itemView) {


        var nameView: TextView? = itemView.findViewById(R.id.category_detail)


        var priceView: TextView? = null

        fun bind(snapshot: DocumentSnapshot?,
                 listener: OnItemSelectListener?,
                 twoPane: Boolean) {


            var buf: CategoryFB = snapshot!!.toObject(CategoryFB::class.java)
            //Resources resources = itemView.getResources();


            nameView!!.text=buf.nom
            // Load image
            /*Glide.with(imageView.getContext())
                    .load(restaurant.getPhoto())
                    .into(imageView);*/

            //category_detail.setText(buf.nom);


            // Click listener
            itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onItemSelected(snapshot!!,twoPane)
                }
            }
        }

    }
}