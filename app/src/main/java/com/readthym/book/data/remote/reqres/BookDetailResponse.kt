package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class ReadthymDetailBookResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: BookData,
    @SerializedName("status_code")
    val statusCode: Int
) {
}