package com.readthym.book.ui.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.book.data.remote.RemoteDataSource
import com.readthym.book.data.remote.reqres.*
import com.readthym.book.utils.Network
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class DetailBookViewModel(val ds: RemoteDataSource) : ViewModel() {

    private var _bookDetailLiveData =
        MutableLiveData<QumparanResource<ReadthymDetailBookResponse?>>()
    val bookDetailLiveData get() = _bookDetailLiveData

    private var _authorLiveData =
        MutableLiveData<QumparanResource<AuthorResponse?>>()
    val authorLiveData get() = _authorLiveData

    private var _favoriteCheckLiveData =
        MutableLiveData<QumparanResource<CheckFavoriteResponse?>>()
    val favoriteCheckLiveData get() = _favoriteCheckLiveData

    private var _addToFavoriteLiveData =
        MutableLiveData<QumparanResource<SaveFavoriteResponse?>>()
    val addToFavoriteLiveData get() = _addToFavoriteLiveData

    private var _deleteFromFavoriteLiveData =
        MutableLiveData<QumparanResource<DeleteFavoriteResponse?>>()
    val deleteFromFavoriteLiveData get() = _deleteFromFavoriteLiveData

    private var _deleteBookLiveData =
        MutableLiveData<QumparanResource<GeneralStatusWithMessageResponse?>>()
    val deleteBookLiveDataLiveData get() = _deleteBookLiveData

    private var _storeBookLiveData =
        MutableLiveData<QumparanResource<GeneralStatusWithMessageResponse?>>()
    val storeBookLiveDataLiveData get() = _storeBookLiveData

    private var _updateBookLiveData =
        MutableLiveData<QumparanResource<GeneralStatusWithMessageResponse?>>()
    val updateBookLiveData get() = _updateBookLiveData

    private var _userFavoriteLiveData =
        MutableLiveData<QumparanResource<ListFavoriteBookResponse?>>()
    val userFavoriteLiveData get() = _userFavoriteLiveData

    private var _searchLiveData =
        MutableLiveData<QumparanResource<SearchBookResponse?>>()
    val searchLiveData get() = _searchLiveData


    fun fetchUserFavorite(id: String) = viewModelScope.launch {
        _userFavoriteLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.getUserFavorite(id)
            if (res.isSuccessful) {
                _userFavoriteLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _userFavoriteLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _userFavoriteLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun fetchAuthors() = viewModelScope.launch {
        _authorLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.getAuthors()
            if (res.isSuccessful) {
                _authorLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _authorLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _authorLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun deleteBook(id: String) = viewModelScope.launch {
        _deleteBookLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.deleteBook(id)
            if (res.isSuccessful) {
                _deleteBookLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _deleteBookLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _userFavoriteLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun searchBook(term: String) = viewModelScope.launch {
        _searchLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.searchBook(term)
            if (res.isSuccessful) {
                _searchLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _searchLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _searchLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun fetchDetailBook(id: String) = viewModelScope.launch {
        _bookDetailLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.getBookDetail(id)
            if (res.isSuccessful) {
                _bookDetailLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _bookDetailLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _bookDetailLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun fetchIfFavorite(bookId: String, userId: String) = viewModelScope.launch {
        _favoriteCheckLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.checkIfFavorite(userId = userId, bookId)
            if (res.isSuccessful) {
                _favoriteCheckLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _favoriteCheckLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _favoriteCheckLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun saveFavorite(bookId: String, userId: String) = viewModelScope.launch {
        _addToFavoriteLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.saveFavorite(userId = userId, bookId)
            if (res.isSuccessful) {
                _addToFavoriteLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _addToFavoriteLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _addToFavoriteLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun deleteFromFavorite(bookId: String, userId: String) = viewModelScope.launch {
        _addToFavoriteLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.deleteFavorite(userId = userId, bookId)
            if (res.isSuccessful) {
                _deleteFromFavoriteLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _deleteFromFavoriteLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _deleteFromFavoriteLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }


    fun updateBook(
        id: String,
        title: String?,
        description: String?,
        overview: String?,
        category: String?,
        photo: File?,
        book: File?,
        idAuthor: Int?
    ) {

        fun createPart(value: String): RequestBody =
            value.toRequestBody()

        fun createImagePart(file: File?, name: String): MultipartBody.Part {
            val body = file?.asRequestBody("image/*".toMediaTypeOrNull())
            return if (file == null) MultipartBody.Part.createFormData(
                name,
                file?.name ?: ""
            ) else MultipartBody.Part.createFormData(name, file.name, body!!)
        }

        fun createPdfPart(file: File?, name: String): MultipartBody.Part {
            val body = file?.asRequestBody("application/pdf".toMediaTypeOrNull())
            return if (file == null) MultipartBody.Part.createFormData(
                name,
                file?.name ?: ""
            ) else MultipartBody.Part.createFormData(name, file.name, body!!)
        }

        val mapBody = mutableMapOf<String, RequestBody>()


        var bookFile: MultipartBody.Part? = null
        var imageFile: MultipartBody.Part? = null

        bookFile = createPdfPart(book, "book")
        imageFile = createImagePart(photo, "photo")

        createImagePart(null, "dumdum")

        if (title != null)
            mapBody.put("title", createPart(title))

        if (description != null)
            mapBody.put("description", createPart(description))

        if (overview != null)
            mapBody.put("overview", createPart(overview))

        if (category != null)
            mapBody.put("category", createPart(category))

        if (idAuthor != null)
            mapBody.put("id_author", createPart(idAuthor.toString()))

        mapBody.put("huwala_humbala", createPart("huawaaaaaa"))


        viewModelScope.launch {
            _updateBookLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.updateBook(
                    id = id,
                    book = bookFile,
                    photo = imageFile,
                    bodyPost = mapBody
                )
                if (res.isSuccessful) {
                    _updateBookLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _updateBookLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _updateBookLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun storeBook(
        title: String,
        description: String,
        overview: String,
        category: String,
        photo: File,
        book: File,
        idAuthor: String
    ) {

        fun createPart(value: String): RequestBody =
            value.toRequestBody()

        fun createImagePart(file: File?, name: String): MultipartBody.Part {
            val body = file?.asRequestBody("image/*".toMediaTypeOrNull())
            return if (file == null) MultipartBody.Part.createFormData(
                name,
                file?.name ?: ""
            ) else MultipartBody.Part.createFormData(name, file.name, body!!)
        }

        fun createPdfPart(file: File?, name: String): MultipartBody.Part {
            val body = file?.asRequestBody("application/pdf".toMediaTypeOrNull())
            return if (file == null) MultipartBody.Part.createFormData(
                name,
                file?.name ?: ""
            ) else MultipartBody.Part.createFormData(name, file.name, body!!)
        }

        val bodyPart = mapOf(
            "title" to createPart(title),
            "description" to createPart(description),
            "overview" to createPart(overview),
            "category" to createPart(category),
            "id_author" to createPart(idAuthor),
        )
        viewModelScope.launch {
            _storeBookLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.storeBook(
                    book = createPdfPart(book, "book"),
                    photo = createImagePart(photo, "photo"),
                    bodyPost = bodyPart
                )
                if (res.isSuccessful) {
                    _storeBookLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _storeBookLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _storeBookLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun resetErrorLiveData() {
        _deleteBookLiveData.postValue(QumparanResource.Default())
    }

}
