package com.viwath.jwtauth.viewModel

data class AuthState(
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signInUsername: String = "",
    val signInPassword: String = ""
)
