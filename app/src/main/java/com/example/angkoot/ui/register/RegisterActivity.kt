package com.example.angkoot.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var activityRegisterBinding : ActivityRegisterBinding? = null
    private val binding get() = activityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide();

        activityRegisterBinding!!.btnSignUp.setOnClickListener{
            Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show()
        }
    }
}