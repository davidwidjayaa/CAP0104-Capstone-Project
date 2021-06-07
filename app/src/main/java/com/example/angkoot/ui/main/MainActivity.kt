package com.example.angkoot.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.databinding.ActivityMainBinding
import com.example.angkoot.ui.login.LoginActivity
import com.example.angkoot.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }


}