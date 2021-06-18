package com.ahmed.airlinesdetails.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.databinding.ActivityMainBinding
import com.ahmed.airlinesdetails.utils.popStackAllInstance

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    lateinit var navHostFragment: Fragment
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(dataBinding.toolbar as Toolbar?)
        supportActionBar?.title = ""
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        navController = findNavController(R.id.navHostFragment)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as Fragment

        mainViewModel.navDestination.observe(this) {
                navController.navigate(it.destinationId, it.bundle)
        }
    }

    override fun onBackPressed() {
        if (navController.currentBackStackEntry?.destination?.id != null) {
            navController.popStackAllInstance(navController.currentBackStackEntry?.destination?.id!!, true)
        } else {
            navController.popBackStack()
        }
        super.onBackPressed()
    }

}