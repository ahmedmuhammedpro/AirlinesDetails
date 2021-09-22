package com.ahmed.airlinesdetails.main_view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(dataBinding.toolbar as Toolbar?)
        supportActionBar?.title = ""
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        mainViewModel.navDestination.observe(this) {
            navController.navigate(it.destinationId, it.bundle)
        }
    }

    fun setupToolbarTitle(title: String) {
        dataBinding.toolbar.findViewById<TextView>(R.id.toolbar_title).text = title
    }

}