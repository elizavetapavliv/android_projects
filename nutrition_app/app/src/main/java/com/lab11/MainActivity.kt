package com.lab11

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.lab11.ui.main.SearchRecipeFragment
import com.lab11.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity(){

    private lateinit var viewPager: ViewPager
    private lateinit var tabs:TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        checkInternetConnection()
    }

    private fun checkInternetConnection(){
        if(!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet connection. See your saved recipes",
                Toast.LENGTH_LONG).show()
            val fragment = supportFragmentManager.findFragmentById(R.id.search_recipe) as SearchRecipeFragment
            fragment.turnOffButtons()
        }
    }

    private fun isNetworkAvailable() : Boolean {
        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null
    }
}