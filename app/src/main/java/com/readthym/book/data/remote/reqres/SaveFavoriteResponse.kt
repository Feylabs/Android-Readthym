package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class SaveFavoriteResponse(
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
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_book")
        val idBook: String,
        @SerializedName("id_user")
        val idUser: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}