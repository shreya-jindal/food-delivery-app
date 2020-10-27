package com.shreya.foodapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.shreya.foodapp.R
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.shreya.foodapp.fragment.*


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView:NavigationView
    lateinit var toolbar: Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frame: FrameLayout
    lateinit var sharedPreferences: SharedPreferences
    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        navigationView=findViewById(R.id.navigationView)
        toolbar=findViewById(R.id.toolbar)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        frame=findViewById(R.id.frame)
        sharedPreferences=getSharedPreferences("FoodappPreference", Context.MODE_PRIVATE)

        setUpToolbar()

        val actionBarDrawerToggle=ActionBarDrawerToggle(
            this@MainActivity,drawerLayout,R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        openHome()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem!!.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it


            when(it.itemId){
                R.id.home->{
                    openHome()
                }
                R.id.myProfile->{
                    supportActionBar?.title="Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.frame,MyProfileFragment()).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.favRestraunts->{
                    supportActionBar?.title="Favourite Restraunts"
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FavRestFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory->{
                    supportActionBar?.title="Order History"
                    supportFragmentManager.beginTransaction().replace(R.id.frame,OrderHistoryFragment()).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.FAQs->{
                    supportActionBar?.title="FAQs"
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FAQsFragment()).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.logOut->{

                    val dialog = AlertDialog.Builder(this@MainActivity as Context)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are ou sure you want to Log Out?")
                    dialog.setPositiveButton("Yes") { text, listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No"){text,listener->
                        drawerLayout.closeDrawers()
                    }
                    dialog.create()
                    dialog.show()


                }


            }

            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }



    fun openHome(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment()).commit()
        supportActionBar?.title="Home"
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is HomeFragment->openHome()
            else-> super.onBackPressed()
        }
    }
}
