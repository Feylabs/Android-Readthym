package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
) {
    data class User(
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}