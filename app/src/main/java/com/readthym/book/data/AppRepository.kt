package com.readthym.book.data

import com.readthym.book.data.remote.RemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.PartMap

class AppRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun login(email: String, password: String) =
        remoteDs.login(email = email, password = password)

}