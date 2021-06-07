package com.example.angkoot.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.utils.EditTextInputUtils
import com.example.angkoot.utils.ToastUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()
    }

    private fun setupUI() {
        supportActionBar?.hide()

        with(binding) {
            edtUsernameLogin.addTextChangedListener(usernameTextWatcher)
            edtPasswordLogin.addTextChangedListener(passwordTextWatcher)

            btnLogin.setOnClickListener {
                ToastUtils.show(applicationContext, getString(R.string.login_success_message))
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            with(EditTextInputUtils) {
                with(binding) {

                    usernameText.observe(this@LoginActivity) { username ->
                        if (!isUsernameValid(username))
                            setError(edtUsernameLogin, getString(R.string.edt_username_message))
                        else
                            clearError(edtUsernameLogin)
                    }

                    passwordText.observe(this@LoginActivity) { password ->
                        if (!isPasswordValid(password))
                            setError(edtPasswordLogin, getString(R.string.edt_password_message))
                        else
                            clearError(edtPasswordLogin)
                    }
                }
            }
        }
    }

    private var usernameTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpString != null)
                viewModel.setUsernameText(tmpString.toString())
        }
    }

    private var passwordTextWatcher: TextWatcher? = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(tmpString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (tmpString != null)
                viewModel.setPasswordText(tmpString.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding) {
            edtUsernameLogin.removeTextChangedListener(usernameTextWatcher)
            usernameTextWatcher = null
            edtPasswordLogin.removeTextChangedListener(passwordTextWatcher)
            passwordTextWatcher = null
        }
    }
}