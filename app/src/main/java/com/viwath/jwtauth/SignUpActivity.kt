package com.viwath.jwtauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.viwath.jwtauth.repository.AuthResult
import com.viwath.jwtauth.ui.AuthUIEvent
import com.viwath.jwtauth.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    // UI element
    private lateinit var btSignUp: Button
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    //
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Find ID
        btSignUp = findViewById(R.id.btSignUp)
        edtUsername = findViewById(R.id.edtSignUpUsername)
        edtPassword = findViewById(R.id.edtSignUpPassword)
        //
        val state = viewModel.state

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.authResult.collect{ result ->
                when(result){
                    is AuthResult.Authorized -> {
                        startActivity(
                            Intent(
                                this@SignUpActivity,
                                MainActivity::class.java
                            )
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        runOnUiThread{
                            Toast.makeText(
                                this@SignUpActivity,
                                "You are not authorize",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    is AuthResult.UnknownError -> {
                        runOnUiThread {
                            Toast.makeText(
                                this@SignUpActivity,
                                "An unknown error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        edtUsername.setText(state.signUpUsername)
        edtPassword.setText(state.signUpPassword)
        // Button event
        btSignUp.setOnClickListener {
            val usernameText = edtUsername.text.toString()
            val passwordText = edtPassword.text.toString()
            viewModel.onEvent(AuthUIEvent.SignUpUsernameChanged(usernameText.toString()))
            viewModel.onEvent(AuthUIEvent.SignUpPasswordChanged(passwordText.toString()))

            val dialog = AlertDialog.Builder(this)
            if (passwordText.length < 8 || passwordText.isBlank())
                return@setOnClickListener dialog
                    .setMessage("Password should not empty or less than 8.")
                    .setTitle("Check")
                    .setNegativeButton("Cancel"){ _, _ ->
                        dialog.create().dismiss()
                    }
                    .create()
                    .show()
            viewModel.onEvent(AuthUIEvent.SignUp)
        }
    }
}