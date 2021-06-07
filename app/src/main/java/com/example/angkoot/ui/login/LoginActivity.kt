package com.example.angkoot.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.utils.ToastUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            btnLogin.setOnClickListener {
                ToastUtils.show(applicationContext, getString(R.string.login_success_message))
            }
        }
    }

}