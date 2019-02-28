package com.nineworldsdeep.gauntlet.synergy.v6

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.nineworldsdeep.gauntlet.R

class SynergyV6Activity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SynergyItemAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    val NEW_ITEM_ACTIVITY_REQUEST_CODE = 1

    private var synergyItemViewModel: SynergyItemViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synergy_v6)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // THIS SECTION IS BREAKING THE BUILD. RUNS WITHOUT IT BUT THERES A LAYOUT GAP
        // NEED TO EXPERIMENT FURTHER AND FIGURE OUT WHAT'S BEST
        // TO SEE WHAT IT'S DOING, JUST UNCOMMENT THE LINES BELOW
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        val actionBar: ActionBar? = supportActionBar
//        actionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
//        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        synergyItemViewModel = ViewModelProviders.of(this).get(SynergyItemViewModel::class.java)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, SynergyFragment.newInstance())
//                    .commitNow()
//        }

        //ONLY NEEDED IN DEMO
        //val demoArray: Array<String> = arrayOf("test", "items", "and stuff")

        viewManager = LinearLayoutManager(this)


        //ONLY NEEDED IN DEMO...
        //viewAdapter = SynergyItemDemoAdapter(demoArray)
        //...REPLACED BY THIS
        viewAdapter = SynergyItemAdapter(this)


        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        synergyItemViewModel?.allItems?.observe(this, object : Observer<List<SynergyItem>> {

            override fun onChanged(synergyItems: List<SynergyItem>?) {
                viewAdapter.setItems(synergyItems!!)
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SynergyV6NewItemActivity::class.java)
            startActivityForResult(intent, NEW_ITEM_ACTIVITY_REQUEST_CODE)
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->

            // set item as selected to persist highlight
            menuItem.isChecked = true

            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            // add code here to update UI, such as swapping fragments
            // on selection

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == NEW_ITEM_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val itemId = data.getStringExtra(SynergyV6NewItemActivity.EXTRA_ITEM_ID)
            val itemValue = data.getStringExtra(SynergyV6NewItemActivity.EXTRA_ITEM_VALUE)
            val itemType = data.getStringExtra(SynergyV6NewItemActivity.EXTRA_ITEM_TYPE)
            val itemLastModified = TimeStampV6.nowString()

            val newItem = SynergyItem(itemId,itemValue,itemType,itemLastModified)

            synergyItemViewModel?.insert(newItem)

        } else {

            Toast.makeText(
                    applicationContext,
                    R.string.empty_item_value_discarded,
                    Toast.LENGTH_LONG).show()
        }
    }
}
