package com.example.angkoot.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.databinding.ActivityMainBinding
import com.example.angkoot.ui.login.LoginActivity
import com.example.angkoot.ui.register.RegisterActivity
import com.example.angkoot.utils.PermissionUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _activity = this

        supportActionBar?.hide()

        with(binding) {
            btnGotoLogin.setOnClickListener {
                Intent(this@MainActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                }
            }

            btnGotoSignUp.setOnClickListener {
                Intent(this@MainActivity, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        requestPermission()
    }

    // PERMISSIONS
    private fun requestPermission() {
        if (PermissionUtils.hasLocationPermission(applicationContext)) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                PermissionUtils.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                PermissionUtils.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        private var _activity: MainActivity? = null

        fun getInstance(): MainActivity? =
            _activity
    }
}