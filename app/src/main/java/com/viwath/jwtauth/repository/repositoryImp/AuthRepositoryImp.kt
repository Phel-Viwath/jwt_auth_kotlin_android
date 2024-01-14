package com.viwath.jwtauth.repository.repositoryImp

import android.content.SharedPreferences
import android.util.Log
import com.viwath.jwtauth.model.AuthRequest
import com.viwath.jwtauth.repository.AuthApi
import com.viwath.jwtauth.repository.AuthRepository
import com.viwath.jwtauth.repository.AuthResult
import retrofit2.HttpException

class AuthRepositoryImp(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return try {
             val response = api.signUp(
                AuthRequest(
                    username = username,
                    password = password
                )
            )
            Log.d("SignUp Response Code:", response.code().toString())
            Log.d("SignUp Response Body:", response.body().toString())
            Log.d("The fucking bug is at:", "Error SignUp")
            signIn(username, password)
        }catch (e: HttpException){
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val tokenResponse = api.signIn(
                AuthRequest(
                    username = username,
                    password = password
                )
            )
            Log.d("SignIn Response Code:", tokenResponse.code().toString())
            Log.d("SignIn Response Body:", tokenResponse.body().toString())
            val token = tokenResponse.body()?.token
            if (token != null){
                prefs.edit().putString("jwt", token).apply()
                AuthResult.Authorized()
            }else{
                Log.d("The fucking bug is at:", "Error Sign In")
                AuthResult.UnknownError()
            }
        }catch (e: HttpException){
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            Log.d("TOKEN Save in ShearPreferences: ", token)
            AuthResult.Authorized()
        }catch (e: HttpException){
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        }
    }
}