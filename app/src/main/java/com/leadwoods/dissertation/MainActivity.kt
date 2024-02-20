package com.leadwoods.dissertation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leadwoods.dissertation.databinding.ActivityMainBinding
import com.leadwoods.dissertation.support.Logger
import com.leadwoods.dissertation.support.disableAR
import com.leadwoods.dissertation.support.enableAR
import com.leadwoods.dissertation.ui.ar_view.ARCoreHelper
import com.leadwoods.dissertation.ui.ar_view.ARRenderer

class MainActivity : Base() {

    private lateinit var binding: ActivityMainBinding

    lateinit var arCoreHelper: ARCoreHelper
    //    lateinit var view: ARViewer
    lateinit var renderer: ARRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.d("called")
        super.onCreate(savedInstanceState)

        init()

        arCoreHelper = ARCoreHelper(this)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.TurnTracker_Fragment, R.id.Library_Fragment, R.id.AR_Fragment, R.id.PlayerOverview_Fragment, R.id.Settings_Fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableAR()
        } else {
            disableAR()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.primary_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.exit_session -> {
                Toast.makeText(this, "Exiting Session", Toast.LENGTH_SHORT).show()
                 true
            }
            else -> {
                throw Exception("Unexpected menu selection")
            }
        }
    }
}