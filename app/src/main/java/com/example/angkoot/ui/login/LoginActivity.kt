package com.example.angkoot.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityLoginBinding
import com.example.angkoot.domain.model.UserModel
import com.example.angkoot.ui.home.HomeActivity
import com.example.angkoot.ui.main.MainActivity
import com.example.angkoot.utils.EditTextInputUtils
import com.example.angkoot.utils.ToastUtils
import com.example.angkoot.utils.ext.hide
import com.example.angkoot.utils.ext.isAllTrue
import com.example.angkoot.utils.ext.show
import com.example.angkoot.utils.ext.text
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()
        setupFirebaseDatabase()
    }

    private fun setupFirebaseDatabase() {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("USERS")
    }

    private fun setupUI() {
        supportActionBar?.hide()

        with(binding) {
            edtUsernameLogin.addTextChangedListener(usernameTextWatcher)
            edtPasswordLogin.addTextChangedListener(passwordTextWatcher)

            btnLogin.setOnClickListener {
                progressbar.show()
                btnLogin.isEnabled = false

                Log.i("firebase", "Find: " + edtUsernameLogin.text.toString())
                reference.child(edtUsernameLogin.text.toString()).get().addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")

                    if (it.value == null) {
                        //login failed
                        ToastUtils.show(
                            applicationContext,
                            getString(R.string.login_failed_wrong_credentials_message)
                        )
                        progressbar.hide()
                        btnLogin.isEnabled = true
                    } else {
                        it.children.forEach { dt ->
                            run {
                                if (dt.child("password").value?.equals(edtPasswordLogin.text()) == true) {
                                    Log.i("firebase", "Got value2 ${dt.child("password").value}")

                                    //login success
                                    // get user info
                                    val password = dt.child("password").value as String
                                    val phone = dt.child("phone").value as String
                                    val username = dt.child("username").value as String

                                    val currentUser = UserModel(
                                        phone,
                                        username,
                                        password
                                    )

                                    //move to home
                                    with(Intent(applicationContext, HomeActivity::class.java)) {
                                        putExtra(HomeActivity.PARAMS_USER, currentUser)
                                        startActivity(this)
                                        finish()
                                        MainActivity.getInstance()?.finish()
                                    }

                                    ToastUtils.show(
                                        applicationContext,
                                        getString(R.string.login_success_message)
                                    )

                                    progressbar.hide()
                                    btnLogin.isEnabled = true
                                } else {
                                    //login failed
                                    ToastUtils.show(
                                        applicationContext,
                                        getString(R.string.login_failed_wrong_credentials_message)
                                    )
                                    progressbar.hide()
                                    btnLogin.isEnabled = true
                                }
                            }
                        }
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data or data not found", it)
                    ToastUtils.show(
                        applicationContext,
                        getString(R.string.login_failed_message)
                    )
                    progressbar.hide()
                    btnLogin.isEnabled = true
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            with(EditTextInputUtils) {
                with(binding) {

                    usernameText.observe(this@LoginActivity) { username ->
                        if (!isUsernameValid(username)) {
                            setError(
                                edtUsernameLogin,
                                getString(R.string.edt_username_error_message)
                            )
                            validateUsername(false)
                        } else {
                            clearError(edtUsernameLogin)
                            validateUsername(true)
                        }
                    }

                    passwordText.observe(this@LoginActivity) { password ->
                        if (!isPasswordValid(password)) {
                            setError(
                                edtPasswordLogin,
                                getString(R.string.edt_password_error_message)
                            )
                            validatePassword(false)
                        } else {
                            clearError(edtPasswordLogin)
                            validatePassword(true)
                        }
                    }

                    areAllInputsValid.observe(this@LoginActivity) { validState ->
                        btnLogin.isEnabled = validState.isAllTrue()
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