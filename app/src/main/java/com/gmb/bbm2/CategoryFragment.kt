package com.gmb.bbm2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.gmb.bbm2.data.model.CategoryFB
import com.gmb.bbm2.tools.allstatic.getFireStoreEditor
import com.gmb.bbm2.tools.firestore.MyFirestoreEditor
import com.gmb.mycalcolibrary.SimpleCalco2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.lyt_title_button_gen.view.*
import org.json.JSONObject
import java.text.NumberFormat

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * [CategoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : DialogFragment(),View.OnClickListener {
    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.cmdCancel -> {
                dismiss.invoke()
            }
            R.id.cmdSave -> {
                saveData()
            }

            R.id.txtMontant -> {
                showDialogCalco()
            }
        }
    }



    // The URL to +1.  Must be a valid URL.
    private val PLUS_ONE_URL = "http://developer.android.com"

    private var objAsJson: String? = null
    private var goToListOnClose=true
    //private var mPlusOneButton: PlusOneButton? = null
    private lateinit var  cmdOk:Button
    private lateinit var cmdCancel:Button
    private  var idCour:String?=null
    private lateinit var rootView:View

    private lateinit var fbEdit:MyFirestoreEditor
    private var numFor:NumberFormat?= NumberFormat.getCurrencyInstance()

    private val calcoListener = SimpleCalco2.OnCalcListener { v -> txtMontant.setText(numFor!!.format(v)) }

    private var mListener: OnCategoryFragmentListener? = null



    public fun setListner(listener: OnCategoryFragmentListener){mListener=listener}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (arguments != null) {
            objAsJson = arguments.getString(ARG_OBJ_AS_JSON)
        }*/

        arguments?.let {
            if (it.containsKey(CategoryFragment.ARG_OBJ_AS_JSON)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                objAsJson = it.getString(CategoryFragment.ARG_OBJ_AS_JSON)
                //activity?.toolbar_layout?.title = mItem?.content
                Log.e("CategoryF","this is arg->"+objAsJson)
            }
        }
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         rootView = inflater!!.inflate(R.layout.fragment_category, container, false)


        //Find the +1 button
       // mPlusOneButton = rootView.findViewById(R.id.plus_one_button) as PlusOneButton

        cmdCancel = rootView.findViewById(R.id.cmdCancel) as Button
        cmdOk = rootView.findViewById(R.id.cmdSave) as Button

        rootView.cmdCancel!!.setOnClickListener(this)
        rootView.cmdSave!!.setOnClickListener(this)


        if(objAsJson!=null && !objAsJson.equals("")){


             var json:JSONObject?= JSONObject(objAsJson)


            idCour=json!!.getString("id")
            rootView.txtNom.setText(json!!.getString("nom"))
            rootView.txtMontant.setText(json!!.getString("montant"))
            rootView.swtOverdue.isSelected=json!!.getBoolean("overdue")

        }


        fbEdit= getFireStoreEditor(this.context)

        return rootView
    }


    var dismiss: (() -> Unit) = {}

    override fun onResume() {
        super.onResume()

        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton!!.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE)
    }


    /*fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }*/


    fun onNewCatSave(id:String,value:String) {
        if (mListener != null) {
            mListener!!.newCatCreated(id,value)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnCategoryFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    internal fun showDialogCalco() {
        //int mStackLevel= new Random().nextInt(100);

        val dialog = SimpleCalco2(activity, calcoListener)
        dialog.show()


        /*// DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);



        // Create and show the dialog.
        DialogFragment newFragment = SimpleCalcoFragment.newInstance("","");
        newFragment.show(ft, "dialog");*/
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    fun saveData(){



        if("".equals(rootView.txtNom.text.toString(),true) || "".equals(rootView.txtMontant.text.toString(),true)){

            Snackbar.make(rootView, getString(R.string.must_fill_out_field),Snackbar.LENGTH_SHORT).show()
            return
        }

        var cat:CategoryFB= CategoryFB();

        cat!!.nom=rootView.txtNom.text.toString();
        //cat!!.minamount=numFor!!.parse(rootView.txtMontant.text.toString()).toDouble()
        cat!!.minamount=(rootView.txtMontant.text.toString()).toDouble()
        cat!!.allowoverdue=rootView.swtOverdue.isChecked

        cat.nomaffiche=cat.nom

        cat.id=idCour;

        var docRef:DocumentReference?=null

        if(idCour!=null){

            docRef=fbEdit.getCollectionRef(MyFirestoreEditor.COLLECTION_CATEGORY).document(cat!!.id)
        }
        else{
            docRef=fbEdit.getCollectionRef(MyFirestoreEditor.COLLECTION_CATEGORY).document()
        }

        //update ID in case it wasn't already one
        cat!!.id=docRef!!.id

        var listner = OnCompleteListener<Void> { task ->
            /*if (progressDialog != null && progressDialog.isShowing()) {

                progressDialog.dismiss()
            }*/

            if (task.isSuccessful) {

                //Toast.makeText(context, getString(R.string.op_completed), Toast.LENGTH_SHORT).show()
                Snackbar.make(rootView, getString(R.string.op_completed),Snackbar.LENGTH_SHORT).show()

                onNewCatSave(cat.id,cat.nomaffiche)
                //Log.d(TAG, "Write batch succeeded.");
                //closefragment()
            } else {
                //Toast.makeText(context, getString(R.string.op_failed) + "->" + task.exception, Toast.LENGTH_SHORT).show()
                Log.w("AddStatement", "write batch failed.", task.exception)

                //hideKeyboard()
                Snackbar.make(rootView, getString(R.string.op_failed),Snackbar.LENGTH_SHORT).show()
            }
        };


        fbEdit.addRecord(docRef,cat,listner)


        closefragment()
    }


    private fun closefragment() {



        dismiss.invoke()

        /* FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();*/

        /*val datCour = System.currentTimeMillis()

        val newFragment: AllListingFragment*/

        /*if (isVertical) {


            if (sens.equals(getString(R.string.sens_credit), ignoreCase = true)) {

                mListener.callNavigationFromFragment(Accueil2Activity.MENU_ITEM_INCOME)
            } else {

                mListener.callNavigationFromFragment(Accueil2Activity.MENU_ITEM_EXPENSE)
            }

        } else {

            //mListener.dataUpdated(catcour.getId(),montant);
        }*/
        if(goToListOnClose){
            val inten= Intent(this.context,CategoryListActivity::class.java);
            startActivity(inten);
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
    interface OnCategoryFragmentListener {

        fun newCatCreated( id:String, name:String);
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        public val ARG_OBJ_AS_JSON = "objAsJson"

        // The request code must be 0 or greater.
        private val PLUS_ONE_REQUEST_CODE = 0

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param objAsJson Parameter 1.
         * @return A new instance of fragment CategoryFragment.
         */
        fun newInstance(objAsJson: String): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putString(ARG_OBJ_AS_JSON, objAsJson)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(objAsJson: String,myListner:OnCategoryFragmentListener,gotoList:Boolean): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putString(ARG_OBJ_AS_JSON, objAsJson)
            fragment.arguments = args
            fragment.mListener=myListner
            fragment.goToListOnClose=gotoList
            return fragment
        }
    }

}// Required empty public constructor
