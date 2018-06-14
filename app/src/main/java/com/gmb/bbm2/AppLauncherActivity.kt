package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_app_launcher.*

class AppLauncherActivity : AppCompatActivity() {

    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null

    val ANONYMO = "anonymous"
    private var mUsername: String? = null
    private var mPhotoUrl: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_launcher)
        setSupportActionBar(toolbar)


        mUsername = ANONYMO

        checkAuthentifiedUser()


        Log.e("AppLauncher","this is the timeLine->"+System.currentTimeMillis());

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    fun checkAuthentifiedUser(){

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser()
        //Log.e("MainTabActivity","this is FirebaseUser ->"+mFirebaseUser)
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        } else {

            /*mUsername = mFirebaseUser!!.getDisplayName()
            //Log.e("MainTabActivity","this is FirebaseUser name->"+mUsername)
            if (mFirebaseUser!!.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser!!.getPhotoUrl().toString()
            }*/

            startActivity(Intent(this, AccueilBBMActivity::class.java))
            finish()
            return
        }
    }
}
