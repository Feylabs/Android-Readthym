package com.readthym.book.data.remote.reqres

import android.os.Parcelable
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class GeneralContentDetailResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("author")
        val author: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("content")
        val content: CommonContent,
        @SerializedName("created_date")
        val createdDate: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("media")
        val media: @RawValue JsonElement? = null,
        @SerializedName("menu")
        val menu: Menu,
        @SerializedName("publish_date")
        val publishDate: String,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("tags")
        val tags: String,
        @SerializedName("title")
        val title: CommonTitle,
        @SerializedName("updated_date")
        val updatedDate: String,
        @SerializedName("url")
        val url: String?
    ) : Parcelable {

        @Parcelize
        data class Menu(
            @SerializedName("supplemental")
            val supplemental: String,
            @SerializedName("title")
            val title: CommonTitle,
            @SerializedName("url")
            val url: String
        ) : Parcelable {

        }

    }
}

@Parcelize
data class CommonContent(
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
) : Parcelable


@Parcelize
data class CommonTitle(
    @SerializedName("en")
    val en: String?,
    @SerializedName("id")
    val id: String?
) : Parcelable
