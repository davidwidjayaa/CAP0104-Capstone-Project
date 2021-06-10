package com.example.angkoot.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityHomeBinding
import com.example.angkoot.domain.model.UserModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var currentUser: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        currentUser = intent.getParcelableExtra<UserModel>(PARAMS_USER) as UserModel
        Log.d("Hehe", intent.getParcelableExtra<UserModel>(PARAMS_USER)?.username ?: "")

        val homeNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        navController = homeNavHostFragment.findNavController()

        setupHomeBottomNavigationView()
    }

    private fun setupHomeBottomNavigationView() {
        with(binding) {
            val appBarConfig = AppBarConfiguration(
                setOf(
                    R.id.orderingFragment,
                    R.id.profileFragment
                )
            )

            setupActionBarWithNavController(navController, appBarConfig)
            homeBottomNavigation.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    companion object {
        const val PARAMS_USER = "PARAMS_USER"
    }
}