package com.henrylabs.qumparan.data.remote.service

import com.readthym.book.data.remote.reqres.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("api/standar-layanan/{slug}")
    @FormUrlEncoded
    suspend fun getStandarLayanan(
        @Path("slug") slug: String,
        @Header("Authorization") token: String?,
        @Field("perPage") perPage: Int = 999
    ): Response<GeneralContentDetailResponse>

    @POST("api/{slug}")
    suspend fun proceedUrl(
        @Path("slug") slug: String,
        @Header("Authorization") token: String?,
    ): Response<GeneralContentDetailResponse>

    @POST("api/pengajuan-keberatan/create/{id}")
    @FormUrlEncoded
    suspend fun sendPengajuanKeberatan(
        @Path("id") path: String,
        @Field("objectionReason") objectionReason: String,
        @Field("positionCase") positionCase: String,
        @Header("Authorization") token: String?,
    ): Response<GeneralStatusWithMessageResponse>

    @POST("auth/login-unsafe")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<ReadthymLoginResponse>

    @POST("auth/register-unsafe")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @POST("{slug}")
    suspend fun getCommonByUri(
        @Path("slug", encoded = true) slug: String,
        @Header("Authorization") token: String?,
    ): Response<GeneralContentDetailResponse>


    @POST("api/permohonan-informasi/create")
    @FormUrlEncoded
    suspend fun sendDocument(
        @Field("purpose") purpose: String,
        @Field("description") desc: String,
        @Header("Authorization") token: String?,
    ): Response<GeneralStatusWithMessageResponse>

    @POST("api/profile/reset/password")
    @FormUrlEncoded
    @Headers(
        "Accept: application/json",
    )
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Header("Authorization") token: String?,
    ): Response<GeneralStatusWithMessageResponse>

    @POST("api/forgot-password")
    @FormUrlEncoded
    @Headers(
        "Accept: application/json",
    )
    suspend fun forgotPassword(
        @Header("Authorization") token: String?,
        @Field("email") email: String
    ): Response<GeneralStatusWithMessageResponse>


    @Multipart
    @Headers(
        "Accept: application/json",
    )

    @POST("api/register")
    suspend fun register(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part() identifierPhoto: MultipartBody.Part
    ): Response<GeneralStatusWithMessageResponse>


    @GET("book")
    suspend fun getBooks(
    ): Response<ReadthymAllBookResponse>

    @GET("book/{id}")
    suspend fun getBooksById(
        @Path("id") id: String
    ): Response<ReadthymDetailBookResponse>

    @GET("book/{id}/delete")
    suspend fun deleteBook(
        @Path("id") id: String
    ): Response<GeneralStatusWithMessageResponse>

    @Multipart
    @POST("book/store")
    suspend fun storeBook(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part() photo: MultipartBody.Part,
        @Part() book: MultipartBody.Part,
    ): Response<GeneralStatusWithMessageResponse>

    @Multipart
    @POST("book/{id}/update")
    suspend fun updateBook(
        @Path("id") id: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part() photo: MultipartBody.Part?,
        @Part() book: MultipartBody.Part?,
    ): Response<GeneralStatusWithMessageResponse>

    @GET("fav/check")
    suspend fun checkIfFavorite(
        @Query("user_id") userId: String,
        @Query("book_id") bookId: String,
    ): Response<CheckFavoriteResponse>

    @GET("author")
    suspend fun getAuthors(
    ): Response<AuthorResponse>

    @POST("fav/store")
    @FormUrlEncoded
    suspend fun saveFavorite(
        @Field("id_user") idUser: String,
        @Field("id_book") idBook: String
    ): Response<SaveFavoriteResponse>

    @GET("fav/delete")
    suspend fun deleteBook(
        @Query("user_id") userId: String,
        @Query("book_id") bookId: String,
    ): Response<DeleteFavoriteResponse>

    @GET("fav/user/{id}")
    suspend fun getFavUser(
        @Path("id") id: String
    ): Response<ListFavoriteBookResponse>

    @GET("book/search")
    suspend fun searchBook(
        @Query("term") term: String
    ): Response<SearchBookResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("api/register")
    @JvmSuppressWildcards
    suspend fun newRegister(
        @PartMap map: Map<String, RequestBody>
    ): Response<GeneralStatusWithMessageResponse>

}