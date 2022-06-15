package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: List<ResData>,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class ResData(
        @SerializedName("birth_date")
        val birthDate: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("death_date")
        val deathDate: Any,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}