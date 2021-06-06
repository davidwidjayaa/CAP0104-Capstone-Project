package com.example.angkoot.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private var activityLoginBinding : ActivityLoginBinding? = null
    private val binding get() = activityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide();

        activityLoginBinding!!.btnLogin.setOnClickListener{
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
        }


    }

}