package com.gmb.bbm2.tools.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gmb.bbm2.R
import kotlinx.android.synthetic.main.custom_dialog.view.*


/**
 * Created by GMB on 4/26/2018.
 */
class CustomDialogListSelector(context: Context, var title: String) : AlertDialog(context) {


    private lateinit var lst: ArrayList<Object>
    private lateinit var lstID: ArrayList<Object>


    private var valSelected:String?=""
    private lateinit var mlistner: CustomDialogListSelector.onItemSelectListener


    interface onItemSelectListener {

        fun onItemSelected(IDVal:Object,valSelected:Object)

    }

    constructor(context: Context, title:String, listID: ArrayList<Object>, listItem: ArrayList<Object>, listner: CustomDialogListSelector.onItemSelectListener): this(context,title){

        this.lstID=listID
        this.lst=listItem
        mlistner=listner


        if (mlistner != null) {

            this.setView(this.initView())

        }


    }


    fun initView(): View {
        // Inflate the layout for this fragment

        val themeContext = this.context
        val inflater = LayoutInflater.from(themeContext)
         var rootView: View =inflater.inflate(R.layout.custom_dialog, null)

        try {


            /*val adapter = ArrayAdapter<String>(context,
                    R.layout.custom_dialog_item_line, lst)

            val listView = rootView.findViewById(R.id.custom_dialog_list) as ListView?
            listView!!.setAdapter(adapter)*/


            /*val linearLayout = rootView.findViewById<LinearLayout>(R.id.custom_dialog_list_lin)
            Log.e("CustomDialog", "initView -- the val a set was->$lst!!!!!!!!!!!")

            for(str in lst){

                Log.e("CustomDialog", "initView -- the val a set was->$str!!!!!!!!!!!")
                val textView = TextView(context)
                textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                textView.gravity = Gravity.CENTER
                textView.setText(str)
                textView.setOnClickListener { mlistner.onItemSelected(str)

                    dismiss()
                }

                // Add TextView to LinearLayout
                linearLayout?.addView(textView)

            }*/
            // Create TextView programmatically.

            rootView.txt_custom_dialog_title.setText(title)


             var mAdapter: CustomDialogListSelector.CustomDialogAdapter = CustomDialogListSelector.CustomDialogAdapter(lstID,lst, context, mlistner,this)

             //Log.e("CustomDialog", "initView -- the val a set was->$lst!!!!!!!!!!!")

 //recList.setHasFixedSize(true);
             val llm = LinearLayoutManager(context)
             llm.orientation = LinearLayoutManager.VERTICAL
            var recyList:RecyclerView=rootView.findViewById<RecyclerView>(R.id.custom_dialog_list)
            recyList.setLayoutManager(llm)

 //setListAdapter(mAdapter);
            recyList.setAdapter(mAdapter)

             mAdapter.notifyDataSetChanged()


        } catch (ex: Exception) {


        }




        return rootView
    }


    public fun closeDialog(){

        this.dismiss()
    }





































    class CustomDialogAdapter(val context: Context) : RecyclerView.Adapter<ViewHolder>() {

        lateinit var items:ArrayList<Object>
        lateinit var IDs:ArrayList<Object>
        lateinit var listner:CustomDialogListSelector.onItemSelectListener

        lateinit var closeSelect:CustomDialogListSelector

        // Gets the number of animals in the list
        override fun getItemCount(): Int {
            return items.size
        }

        constructor(IDs : ArrayList<Object>,items : ArrayList<Object>,  context: Context, listner: CustomDialogListSelector.onItemSelectListener,closeSelect:CustomDialogListSelector):this(context){

            this.items=items
            this.IDs=IDs
            this.listner=listner
            this.closeSelect=closeSelect

            Log.e("CustomDialogAdapter", "constructor -- the val a set was->$items!!!!!!!!!!!")
        }



        // Inflates the item views
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_dialog_item_line, parent, false),listner,closeSelect )
        }

        // Binds each animal in the ArrayList to a view
        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {



            var buf: String = items.get(position)!!.toString()
            Log.e("CustomDialog", "onBindViewHolder -- the val a set was->$buf!!!!!!!!!!!")
            holder?.txtId!!.text = IDs.get(position)!!.toString()
            holder?.txtContent!!.text = buf

            holder?.txtContent!!.setOnClickListener {
                if (holder.myListner != null) {

                    holder.myListner!!.onItemSelected(IDs.get(position),items.get(position))

                    holder.closeSelect.closeDialog()

                }
            }
        }

    }

    class ViewHolder (view: View,var myListner: CustomDialogListSelector.onItemSelectListener,var closeSelect:CustomDialogListSelector) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        //val tvAnimalType = view.tv_animal_type
        lateinit var  txtId:TextView
        lateinit var txtContent:TextView

        init {

            txtId=view.findViewById(R.id.dialog_line_id)
            txtContent=view.findViewById(R.id.dialog_line_content)
        }

    }
}