package com.viwath.jwtauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.viwath.jwtauth.repository.AuthResult
import com.viwath.jwtauth.ui.AuthUIEvent
import com.viwath.jwtauth.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var btSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        //// Find id
        btSignIn = findViewById(R.id.btSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        //
        val state = viewModel.state

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.authResult.collect{ result ->
                when(result){
                    is AuthResult.Authorized -> {
                        startActivity(
                            Intent(
                                this@SignInActivity,
                                MainActivity::class.java
                            )
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        runOnUiThread{
                            Toast.makeText(
                                this@SignInActivity,
                                "You are not authorize",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else -> {
                        runOnUiThread {
                            Toast.makeText(
                                this@SignInActivity,
                                "An unknown error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
        edtUsername.setText(state.signInUsername)
        edtPassword.setText(state.signInPassword)

        btSignIn.setOnClickListener {
            val usernameText = edtUsername.text
            val passwordText = edtPassword.text

            viewModel.onEvent(AuthUIEvent.SignInUsernameChanged(usernameText.toString()))
            viewModel.onEvent(AuthUIEvent.SignInPasswordChanged(passwordText.toString()))

            viewModel.onEvent(AuthUIEvent.SignIn)
        }

        tvSignUp.setOnClickListener {
            startActivity(
                Intent(
                    this@SignInActivity,
                    SignUpActivity::class.java
                )
            )
        }
    }
}