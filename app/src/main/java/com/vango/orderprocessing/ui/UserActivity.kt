package com.vango.orderprocessing.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.snackbar.Snackbar
import com.vango.orderprocessing.R
import com.vango.orderprocessing.SharedViewModel
import com.vango.orderprocessing.databinding.ActivityUserBinding
import com.vango.orderprocessing.utils.Const
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var navController: NavController
    private val sharedViewModel: SharedViewModel by viewModels()
    private val requestCodeCameraPermission = 1001
    var appBarConfiguration: AppBarConfiguration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED && sharedViewModel.getAccountType() == Const.PHARMACEUTIST
        ) {
            askForCameraPermission()
        }

        setContentView(binding.root)
        initNavigation()
        sharedViewModel.badgeText.observe(this) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
            snackbar.anchorView = binding.bottomNavigation
            snackbar.show()
        }
//        binding.bottomNavigation.getOrCreateBadge(R.id.ordersFragment).number = 1
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Please enable camera permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }

    private fun initNavigation() {
        navController = findNavController(R.id.fragment_nav_host)
        // menu jeżeli użytkownik jest klientem
        if (sharedViewModel.getAccountType() == Const.CLIENT) {
            binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_menu)
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.dashboardFragment,
                R.id.cartFragment,
                R.id.ordersFragment,
                R.id.settingsFragment
            ))
            setupActionBarWithNavController(navController, appBarConfiguration!!)
        } else {
            //menu jeżeli użytkownik jest aptekarzem
            binding.bottomNavigation.inflateMenu(R.menu.admin_bottom_navigation_menu)
            navController.navigate(R.id.action_dashboardFragment_to_pharmaceutistDashboardFragment)
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.pharmaceutistDashboardFragment,
                R.id.scannerFragment,
                R.id.ordersFragment,
                R.id.settingsFragment
            ))
            setupActionBarWithNavController(navController, appBarConfiguration!!)
        }
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it, navController)
            return@setOnItemSelectedListener true
        }

        binding.bottomNavigation.setOnItemReselectedListener {
            navController.popBackStack(it.itemId, false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration!!)
    }



}