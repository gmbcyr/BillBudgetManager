package com.gmb.bbm2.tools.adapter

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.gmb.bbm2.NotifDetailActivity
import com.gmb.bbm2.NotifDetailFragment
import com.gmb.bbm2.R
import com.gmb.bbm2.data.model.OnTimeNotificationFB
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.bbm2.tools.json.MyJsonParserKotlin
import com.gmb.bbm2.tools.listner.MyGestureListner
import com.gmb.bbm2.tools.utils.CustomDialogListSelector
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.notif_line2.view.*
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by GMB on 3/12/2018.
 */
class NotifAdapter(private val query: Query,
                   private val mListener: OnItemSelectListener,
                   private val mTwoPane: Boolean,
                   private val viewRoot:View,
                   private val activity: AppCompatActivity) :
        MyFBadapter<NotifAdapter.ViewHolder>(query),MyAdapterUpdate {


    var fbEdit:MyFirestoreEditor?=null

    init {
        fbEdit= MyFirestoreEditor(activity)
    }

    override fun <T> getItemAtPostion(position: Int): Any {

        return super.getSnapshot(position)!!
    }

    override fun <T> removeFromList(position: Int): Any {

        val buf=getItemAtPostion<Any>(position)!!
        super.snapshot!!.remove(buf)
        super.notifyItemRemoved(position)

        return buf
    }

    override fun getMyAdapterPosition(): Int {

        return super.position
    }

    override fun afficheEltCorrespondantAuChoix(pos: Int) {

        val buf = getItemAtPostion<Any>(pos) as DocumentSnapshot




        showItemSelected(buf,false)
    }

    override fun afficheMenuContextuel(pos: Int) {

        val snap = getItemAtPostion<Any>(pos) as DocumentSnapshot

        val buf = snap.toObject(OnTimeNotificationFB::class.java)

        if (buf != null && buf!!.getPaidop()) {

            Toast.makeText(activity, activity.getString(R.string.cant_edit_this_notif), Toast.LENGTH_LONG).show()

            return

        }






        val listener = object : CustomDialogListSelector.onItemSelectListener {
            override fun onItemSelected(IDVal: Object, valSelected: Object) {


                if(activity.getString(R.string.ctx_mn_edit_val).equals(IDVal.toString())){


                    showItemSelected(snap,false)
                }
                else if(activity.getString(R.string.ctx_mn_pay_val).equals(IDVal.toString())){

                    paid(buf)

                }
                else if(activity.getString(R.string.ctx_mn_delete_val).equals(IDVal.toString())){

                    delete(buf)
                }
                else if(activity.getString(R.string.ctx_mn_delete_all_val).equals(IDVal.toString())){

                    deleteAll(buf)
                }

            }


        }


        val labels=activity.resources.getStringArray(R.array.context_menu)

        val vals=activity.resources.getStringArray(R.array.context_menu_val)


        //val vals=activity.resources.getStringArray(R.array.context_menu_val)
        val myVals=ArrayList<Object>()
        for(str in vals){

            (myVals as ArrayList<String>).add(str)
        }


        val myLabels=ArrayList<Object>()
        for(str in labels){

            (myLabels as ArrayList<String>).add(str)
        }


        val dialog = CustomDialogListSelector(activity, activity.getString(R.string.context_menu_title), myVals, myLabels, listener)
        dialog.show()





    }

    override fun onSwipeRight(pos: Int) {


        val snap = getItemAtPostion<Any>(pos) as DocumentSnapshot
        val buf = snap.toObject(OnTimeNotificationFB::class.java)

        if (buf != null) {

            if (!buf!!.getPaidop()) {

                paid(buf)

                /*buf = removeFromList<Any>(pos) as OnTimeNotification
                // OnTimeNotificationFB buf=re
                //send la notification a alarmService pour qu il mette a jour notif dans la bd
                MyReccurentDBrequest.updateNotification(context, buf!!.getBbmStatement().getId(), buf!!.getId(),
                        MyAlarmService.CREATE, context.getString(R.string.current_state_done),
                        MyAlarmService.CALLER_EVENT_DONE, buf!!.getBbmStatement().getMontant(),
                        buf!!.getTimestart(), true)*/

                return

            }

            Toast.makeText(activity, activity.getString(R.string.cant_edit_this_notif), Toast.LENGTH_LONG).show()

        }
    }

    override fun onSwipeLeft(pos: Int) {


        val snap = getItemAtPostion<Any>(pos) as DocumentSnapshot
        val buf = snap.toObject(OnTimeNotificationFB::class.java)

        if (buf != null) {

            if (!buf!!.getPaidop()) {

                cancel(buf)

                /*buf = removeFromList<Any>(pos) as OnTimeNotification
                // OnTimeNotificationFB buf=re
                //send la notification a alarmService pour qu il mette a jour notif dans la bd
                MyReccurentDBrequest.updateNotification(context, buf!!.getBbmStatement().getId(), buf!!.getId(),
                        MyAlarmService.CREATE, context.getString(R.string.current_state_done),
                        MyAlarmService.CALLER_EVENT_DONE, buf!!.getBbmStatement().getMontant(),
                        buf!!.getTimestart(), true)*/

                return

            }

            Toast.makeText(activity, activity.getString(R.string.cant_edit_this_notif), Toast.LENGTH_LONG).show()

        }
    }


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
        val view= ViewHolder(inflater.inflate(R.layout.notif_line2, parent, false),parent.context,this)



        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), mListener,mTwoPane)

        var buf: OnTimeNotificationFB = getSnapshot(position)!!.toObject(OnTimeNotificationFB::class.java)


    }


    public fun resetQuery(query: Query){

        super.setQuery(query)
    }


     fun showItemSelected(documentSnap: DocumentSnapshot, mTwoPane: Boolean) {


        val buf = documentSnap.toObject(OnTimeNotificationFB::class.java)
        //Resources resources = itemView.getResources();

         if (buf != null && buf!!.getPaidop()) {

             Toast.makeText(activity, activity.getString(R.string.cant_edit_this_notif), Toast.LENGTH_LONG).show()

             return

         }

        val js = JSONObject(MyJsonParserKotlin.onTimeNotifToJsonString(buf))

        /*val item = v.tag as DummyContent.DummyItem*/
        if (mTwoPane) {
            /*val fragment = NotifDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(NotifDetailFragment.ARG_OBJ_AS_JSON, js.toString())
                }
            }*/
            val fragment= NotifDetailFragment.newInstance(js.toString(),js.getString("sens"),js.getLong("datestart").toString(),
                    js.getString("idCat"),js.getString("idStat"),System.currentTimeMillis())
            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notif_detail_container, fragment)
                    .commit()
        } else {

            val intent = Intent(viewRoot.context, NotifDetailActivity::class.java).apply {
                putExtra(NotifDetailFragment.ARG_OBJ_AS_JSON, js.toString())
                putExtra(NotifDetailFragment.ARG_SENS,js.getString("sens"))
                putExtra(NotifDetailFragment.ARG_DATE_START_PARAM,js.getLong("datestart").toString())
                putExtra(NotifDetailFragment.ARG_ID_CAT,js.getString("idcat"))
                putExtra(NotifDetailFragment.ARG_ID_STAT,js.getString("idstate"))
                putExtra(NotifDetailFragment.ARG_ITEM_ID,js.getString("id"))
            }
            Log.e("NotifList", "this is what a send ->"+js.toString())
            viewRoot.context.startActivity(intent)

            /*val intent = Intent(this.applicationContext, CategoryDetailActivity::class.java).apply {
                putExtra(CategoryFragment.ARG_OBJ_AS_JSON, js.toString())
            }
            startActivity(intent)*/
        }
    }


    fun paid( notif: OnTimeNotificationFB) {

        try {


            fbEdit!!.payNotif(notif)

        } catch (ex: Exception) {


        }

    }

    fun cancel( notif: OnTimeNotificationFB) {

        try {


            fbEdit!!.cancelNotif(notif)

        } catch (ex: Exception) {


        }

    }

    fun delete(notif: OnTimeNotificationFB) {

        try {


            fbEdit!!.deleteNotif(notif)
        } catch (ex: Exception) {


        }

    }


    fun deleteAll(notif: OnTimeNotificationFB) {

        try {

            fbEdit!!.deleteNotif(notif)
        } catch (ex: Exception) {


        }

    }






















    class ViewHolder(itemView: View,private val context: Context,private val myAdapter: MyAdapterUpdate)//ButterKnife.bind(this, itemView);
        : RecyclerView.ViewHolder(itemView),View.OnTouchListener {


        private var mGestureDetector: GestureDetector?=null



        var moiFor=SimpleDateFormat("MMM.")
        var dayFor=SimpleDateFormat("dd")
        var hourFor=SimpleDateFormat("hh:mm")
        var numFor=NumberFormat.getCurrencyInstance()

         init{

             itemView.setOnTouchListener(this)

            val gestureListner = MyGestureListner(context, myAdapter,  this)

            mGestureDetector = GestureDetector(context, gestureListner)

             itemView!!.setOnTouchListener(this)
        }


        fun bind(snapshot: DocumentSnapshot?,
                 listener: OnItemSelectListener?,
                 twoPane: Boolean) {


            var buf: OnTimeNotificationFB = snapshot!!.toObject(OnTimeNotificationFB::class.java)
            //Resources resources = itemView.getResources();


            var montant=buf.montantprojecte
            var montantPaid=buf.montantop

            itemView.txtNotifLineNom.text=buf.nom
            itemView.txtNotifLineNote.text=buf.descrip
            itemView.txtNotifLineMois.text=moiFor.format(Date(buf.datestart))
            itemView.txtNotifLineJour.text=dayFor.format(Date(buf.datestart))
            itemView.txtNotifLineHour.text=hourFor.format(Date(buf.datestart))



            if (buf.getSens().equals("credit",true)) {

                //viewHolder.montant.setBackgroundColor(Color.GREEN);
                itemView.txtNotifLineMontant.setBackgroundResource(R.drawable.back_notif_income2)

            } else {
                montant=0-montant
                montantPaid=0-montantPaid
                //viewHolder.montant.setBackgroundColor(Color.RED);
                itemView.txtNotifLineMontant.setBackgroundResource(R.drawable.back_notif_expense2)
            }
            itemView.txtNotifLineMontant.text=numFor.format(montant)



            var duDays = "due "

            itemView.imgNotifLinePaid.setVisibility(View.GONE)




            if (buf.paidop) {

                itemView.imgNotifLinePaid.setVisibility(View.VISIBLE)
                itemView.imgNotifLinePaid.getParent().bringChildToFront(itemView.imgNotifLinePaid)
                itemView.imgNotifLinePaid.setImageResource(R.drawable.thump_up)
                itemView.txtNotifLineMontant.text=numFor.format(montantPaid)

                duDays =  context.getString(R.string.paid_item)
            } else {


                itemView.txtNotifLineMontant.text=numFor.format(montant)

                val cal = GregorianCalendar()
                cal.time = Date()
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0)

                val cal2 = GregorianCalendar()
                cal2.timeInMillis = buf.timestart
                cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH), 23, 59)

                var dueTime: Long = 0

                //long dif=buf.getTimestart()-System.currentTimeMillis();

                val dif = cal2.timeInMillis - cal.timeInMillis



                dueTime = dif / 86400000

                if (dueTime < 0) {


                    duDays = (-1 * dueTime).toString() + " " + context.getString(R.string.days) + " " + context.getString(R.string.overdue)

                } else if (dueTime == 0L)
                    duDays = "due " + context.getString(R.string.today)
                else
                    duDays = "due " + dueTime + " " + context.getString(R.string.days)


                if (dueTime <= 0) {


                    if (buf.timestart < System.currentTimeMillis()) {
                        itemView.imgNotifLinePaid.setVisibility(View.VISIBLE)

                        itemView.imgNotifLinePaid.getParent().bringChildToFront(itemView.imgNotifLinePaid)
                        itemView.imgNotifLinePaid.setImageResource(R.drawable.thump_down)

                    }

                }
            }

            itemView.txtNotifLineDue.setText(duDays)

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

            /*itemView.setOnTouchListener() {
                if (listener !== null) {
                    listener!!.onItemSelected(snapshot!!,twoPane)
                }
            }*/
        }




        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {


            //Toast.makeText(context,"CustomCOntextMenuNotif2 position->",Toast.LENGTH_SHORT).show();
            return mGestureDetector!!.onTouchEvent(p1)
        }

    }
}