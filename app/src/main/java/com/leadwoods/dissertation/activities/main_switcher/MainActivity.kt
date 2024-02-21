package com.leadwoods.dissertation.activities.main_switcher

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leadwoods.dissertation.R
import com.leadwoods.dissertation.activities.session_manager.SessionManager
import com.leadwoods.dissertation.databinding.ActivityMainBinding
import com.leadwoods.dissertation.support.Base
import com.leadwoods.dissertation.support.GAME_MASTER
import com.leadwoods.dissertation.support.Logger
import com.leadwoods.dissertation.support.disableAR
import com.leadwoods.dissertation.support.enableAR

class MainActivity : Base() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var arCoreHelper: ARCoreHelper
//    lateinit var view: ARViewer
//    lateinit var renderer: ARRenderer

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.d("called")
        super.onCreate(savedInstanceState)

//        arCoreHelper = ARCoreHelper(this)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            if(sharedPreferences.getBoolean(GAME_MASTER, false))
                setOf(
                    R.id.TurnTracker_Fragment, R.id.Library_Fragment, R.id.AR_Fragment, R.id.PlayerOverview_Fragment, R.id.Settings_Fragment
                )
            else
                setOf(
                    R.id.TurnTracker_Fragment, R.id.Library_Fragment, R.id.AR_Fragment, R.id.LobbyControls_Fragment, R.id.Settings_Fragment
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
                Toast.makeText(this, "I am Definitely Saving Your Data ;)", Toast.LENGTH_SHORT).show()
                this.startActivity(Intent(this, SessionManager::class.java))
                true
            }
            else -> {
                throw Exception("Unexpected menu selection")
            }
        }
    }
}