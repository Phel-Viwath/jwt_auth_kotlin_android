package com.viwath.jwtauth.repository

import com.viwath.jwtauth.model.AuthRequest
import com.viwath.jwtauth.model.AuthResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/v1/auth/register")
    suspend fun signUp(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("/api/v1/auth/authenticate")
    suspend fun signIn(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @GET("/api/v1/auth/admin/hi")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}