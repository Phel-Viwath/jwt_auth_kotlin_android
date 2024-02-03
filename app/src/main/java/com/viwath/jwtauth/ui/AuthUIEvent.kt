package com.viwath.jwtauth.ui



sealed class AuthUIEvent {
    data class SignUpUsernameChanged(val value: String): AuthUIEvent()
    data class SignUpPasswordChanged(val value: String): AuthUIEvent()
    data object SignUp: AuthUIEvent()

    data class SignInUsernameChanged(val value: String): AuthUIEvent()
    data class SignInPasswordChanged(val value: String): AuthUIEvent()
    data object SignIn: AuthUIEvent()
}