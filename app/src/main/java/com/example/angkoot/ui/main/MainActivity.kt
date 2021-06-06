package com.example.angkoot.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityMainBinding
import com.example.angkoot.ui.login.LoginActivity
import com.example.angkoot.ui.register.RegisterActivity

class MainActivity : AppCompatActivity(){

    private var activityMainBinding : ActivityMainBinding? = null
    private val binding get() = activityMainBinding
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide();

        activityMainBinding!!.buttonLogin.setOnClickListener{
            val intent= Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        activityMainBinding!!.buttonSignUp.setOnClickListener{
            val intent= Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


}