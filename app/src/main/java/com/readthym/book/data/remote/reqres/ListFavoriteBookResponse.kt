package com.readthym.book.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class ListFavoriteBookResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: List<FavoriteData>,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class FavoriteData(
        @SerializedName("book")
        val book: Book,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_book")
        val idBook: Int,
        @SerializedName("id_user")
        val idUser: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user")
        val user: User
    ) {
        data class Book(
            @SerializedName("author_desc")
            val authorDesc: String,
            @SerializedName("author_name")
            val authorName: String,
            @SerializedName("category")
            val category: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("id_author")
            val idAuthor: Int,
            @SerializedName("overview")
            val overview: String,
            @SerializedName("pdf_path")
            val pdfPath: String,
            @SerializedName("pdf_path_full")
            val pdfPathFull: String,
            @SerializedName("photo_path")
            val photoPath: String,
            @SerializedName("photo_path_full")
            val photoPathFull: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("updated_at")
            val updatedAt: String
        )

        data class User(
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
}