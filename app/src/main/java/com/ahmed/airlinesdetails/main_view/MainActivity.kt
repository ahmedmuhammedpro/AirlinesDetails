package com.ahmed.airlinesdetails.main_view

import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.ahmed.airlinesdetails.receivers.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var dataBinding: ActivityMainBinding
    private var connectivityReceiver: ConnectivityReceiver? = null
    private var isFirstTimeToReceiver = false

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

        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun setupToolbarTitle(title: String) {
        dataBinding.toolbar.findViewById<TextView>(R.id.toolbar_title).text = title
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            if (isFirstTimeToReceiver) {
                val snackBar = Snackbar.make(dataBinding.root, R.string.network_retrieve, Snackbar.LENGTH_LONG)
                val snackBarTextView = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                snackBarTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wifi_retrieved,0, 0, 0)
                snackBarTextView.compoundDrawablePadding = 28
                snackBar.show()
            }

            isFirstTimeToReceiver = true
        } else {
            val snackBar = Snackbar.make(dataBinding.root, R.string.network_lost, Snackbar.LENGTH_LONG)
            val snackBarTextView = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackBarTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wifi_lost,0, 0, 0)
            snackBarTextView.compoundDrawablePadding = 28
            snackBar.show()
        }

    }

}