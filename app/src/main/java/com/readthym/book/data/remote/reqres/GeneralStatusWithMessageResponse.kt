package com.readthym.book.data.remote.reqres


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class GeneralStatusWithMessageResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,

    // used in login
    @Nullable
    @SerializedName("user")
    val user: LoginSuccessResponse.User?,

    // used in login
    @Nullable
    @SerializedName("token")
    val token: String
)