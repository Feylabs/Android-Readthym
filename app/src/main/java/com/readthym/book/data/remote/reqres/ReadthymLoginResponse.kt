package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class ReadthymLoginResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: ResData,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class ResData(
        @SerializedName("contact")
        val contact: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("photo")
        val photo: Any,
        @SerializedName("role")
        val role: String,
        @SerializedName("status")
        val status: Any,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}