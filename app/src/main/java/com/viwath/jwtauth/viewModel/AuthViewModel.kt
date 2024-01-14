package com.viwath.jwtauth.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viwath.jwtauth.repository.AuthRepository
import com.viwath.jwtauth.repository.AuthResult
import com.viwath.jwtauth.ui.AuthUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(AuthState())
    private val resultChanel = Channel<AuthResult<Unit>>()
    val authResult = resultChanel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthUIEvent){
        when(event){
            is AuthUIEvent.SignUpUsernameChanged -> {
                state = state.copy(signUpUsername = event.value)
            }
            is AuthUIEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.value)
            }
            is AuthUIEvent.SignUp -> {
                signUp()
            }
            is AuthUIEvent.SignInUsernameChanged -> {
                state = state.copy(signInUsername = event.value)
            }
            is AuthUIEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is AuthUIEvent.SignIn -> {
                signIn()
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            val result = repository.signUp(
                username = state.signUpUsername,
                password = state.signUpPassword
            )
            resultChanel.send(result)
        }
    }

    private fun signIn(){
        viewModelScope.launch {
            val result = repository.signIn(
                username = state.signInUsername,
                password = state.signInPassword
            )
            resultChanel.send(result)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            val result = repository.authenticate()
            resultChanel.send(result)
        }
    }
}