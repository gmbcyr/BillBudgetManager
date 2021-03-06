package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_category_detail.*

/**
 * An activity representing a single Category detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [CategoryListActivity].
 */
class CategoryDetailActivity : AppCompatActivity(),CategoryFragment.OnCategoryFragmentListener {
    override fun newCatCreated(id: String, name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)
        setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = CategoryFragment().apply {
                arguments = Bundle().apply {
                    //putString(CategoryFragment.ARG_OBJ_AS_JSON, this.getString(CategoryFragment.ARG_OBJ_AS_JSON))
                    putString(CategoryFragment.ARG_OBJ_AS_JSON, intent.getStringExtra(CategoryFragment.ARG_OBJ_AS_JSON))

                    Log.e("CateDetail","this is the avant de cal->"+intent.getStringExtra(CategoryFragment.ARG_OBJ_AS_JSON))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.category_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    navigateUpTo(Intent(this, CategoryListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
