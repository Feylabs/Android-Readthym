package com.readthym.book.data.remote

import com.henrylabs.qumparan.data.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource(
    private val commonService: ApiService,
) {

    suspend fun login(email: String, password: String) =
        commonService.login(email = email, password = password)

    suspend fun register(name: String, email: String, password: String) =
        commonService.register(name = name, email = email, password = password)

    suspend fun getBooks() =
        commonService.getBooks()

    suspend fun getBookDetail(id: String) =
        commonService.getBooksById(id)

    suspend fun getAuthors() =
        commonService.getAuthors()

    suspend fun deleteBook(id: String) =
        commonService.deleteBook(id)

    suspend fun storeBook(
        bodyPost: Map<String, @JvmSuppressWildcards RequestBody>,
        photo: MultipartBody.Part,
        book: MultipartBody.Part
    ) =
        commonService.storeBook(
            data = bodyPost,
            photo = photo,
            book = book
        )

    suspend fun updateBook(
        id: String,
        bodyPost: Map<String, @JvmSuppressWildcards RequestBody>,
        photo: MultipartBody.Part?,
        book: MultipartBody.Part?
    ) =
        commonService.updateBook(
            id = id,
            data = bodyPost,
            photo = photo,
            book = book
        )

    suspend fun checkIfFavorite(userId: String, bookId: String) = commonService.checkIfFavorite(
        userId = userId, bookId = bookId
    )

    suspend fun saveFavorite(userId: String, bookId: String) = commonService.saveFavorite(
        idBook = bookId, idUser = userId
    )

    suspend fun deleteFavorite(userId: String, bookId: String) =
        commonService.deleteBook(
            userId = userId, bookId = bookId
        )

    suspend fun getUserFavorite(userId: String) =
        commonService.getFavUser(userId)

    suspend fun searchBook(
        term: String
    ) = commonService.searchBook(term)

}