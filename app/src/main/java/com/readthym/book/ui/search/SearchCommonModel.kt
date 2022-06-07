package com.readthym.book.ui.search

data class SearchCommonModel(
    val category: String,
    val description: Description,
    val id: Int,
    val type: String,
    val link: String,
    val endpoint: String,
    val slug: String,
    val tags: String,
    val title: Title
) {
    data class Description(
        val en: String,
        val id: String
    )

    data class Title(
        val en: String,
        val id: String
    )
}