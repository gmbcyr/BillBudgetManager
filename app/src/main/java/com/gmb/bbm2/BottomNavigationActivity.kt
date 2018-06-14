package com.gmb.bbm2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.gmb.bbm2.tools.utils.Filters
import com.gmb.bbm2.tools.utils.OptionFilterFragment
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : AppCompatActivity(),CategoryFragment.OnCategoryFragmentListener,OptionFilterFragment.FilterListener {
    override fun newCatCreated(id: String, name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFilter(filters: Filters) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                val inten= Intent(this,AddBBMActivity::class.java);
                startActivity(inten);
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)


                    val inten= Intent(this,CategoryListActivity::class.java);
                    startActivity(inten);
               /* var range= IntRange(1,10)

                var filter=OptionFilterFragment();

                filter.showMyDialog2(this,"TAG")*/



                return@OnNavigationItemSelectedListener true
            }


            R.id.navigation_frag1 -> {
                message.setText(R.string.title_notifications)


                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID,
                                intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID))
                    }
                }

                supportFragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()

                return@OnNavigationItemSelectedListener true
            }



            R.id.navigation_frag2 -> {
                message.setText(R.string.title_notifications)


                val inten= Intent(this,ItemListActivity::class.java);
                startActivity(inten);

                val fragment = CategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID,
                                intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID))
                    }
                }

                supportFragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
