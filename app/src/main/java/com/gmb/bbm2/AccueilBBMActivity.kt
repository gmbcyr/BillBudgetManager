package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gmb.bbm2.dummy.DummyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main_and_menu.*
import kotlinx.android.synthetic.main.app_bar_main_and_menu.*

class AccueilBBMActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NotifListFragment.OnListFragmentInteractionListener,
                            CustomCalendarFragment.OnListFragmentInteractionListener{



    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null

    val ANONYMO = "anonymous"
    private var mUsername: String? = null
    private var mPhotoUrl: String? = null


    override fun onListFragmentInteraction(item: DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_and_menu)
        setSupportActionBar(toolbar)


        mUsername = ANONYMO

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

            mUsername = mFirebaseUser!!.getDisplayName()
            //Log.e("MainTabActivity","this is FirebaseUser name->"+mUsername)
            if (mFirebaseUser!!.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser!!.getPhotoUrl().toString()
            }
        }


        val fragment = CustomCalendarFragment.newInstance(1)

        this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.accueil_content, fragment)
                .commit()




        fab.setOnClickListener { view ->
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
                /*val intent = Intent(this, NotifDetailActivity::class.java).apply {
                    putExtra(NotifDetailFragment.ARG_OBJ_AS_JSON, "")
                    putExtra(NotifDetailFragment.ARG_SENS,getString(R.string.sens_debit))
                    putExtra(NotifDetailFragment.ARG_DATE_COUR,System.currentTimeMillis())
                    putExtra(NotifDetailFragment.ARG_DATE_START_PARAM,System.currentTimeMillis().toString())



                }
                startActivity(intent)*/

                val fragment = CustomCalendarFragment().apply {
                    arguments = Bundle().apply {
                        putString(NotifDetailFragment.ARG_OBJ_AS_JSON, "")
                    }
                }
                this.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.accueil_content, fragment)
                        .commit()
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_and_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true

            R.id.mi_accueil_logout -> {
                mFirebaseAuth!!.signOut()
                //GoogleSignInApi.signOut(mGoogleApiClient)
                mUsername = ANONYMO
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

                val inten= Intent(this,NotifListActivity::class.java);
                startActivity(inten);

            }
            R.id.nav_slideshow -> {

                val inten= Intent(this,CategoryListActivity::class.java);
                startActivity(inten);

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
