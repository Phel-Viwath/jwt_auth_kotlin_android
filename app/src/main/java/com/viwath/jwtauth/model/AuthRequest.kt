package com.viwath.jwtauth.model

data class AuthRequest(
    val username: String,
    val password: String
)