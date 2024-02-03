package com.viwath.jwtauth.repository

/* Sealed Class is the special class that we can use
   "When" condition to check the result in inner class
 */
sealed class AuthResult <T>(val data: T? = null){
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class UnknownError<T>: AuthResult<T>()
}