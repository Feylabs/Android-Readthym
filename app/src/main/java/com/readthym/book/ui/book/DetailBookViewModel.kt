package com.readthym.book.ui.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.book.data.remote.RemoteDataSource
import com.readthym.book.data.remote.reqres.*
import com.readthym.book.utils.Network
import kotlinx.coroutines.launch
import org.json.JSONObject

class DetailBookViewModel(val ds: RemoteDataSource) : ViewModel() {

    private var _bookDetailLiveData =
        MutableLiveData<QumparanResource<ReadthymDetailBookResponse?>>()
    val bookDetailLiveData get() = _bookDetailLiveData

    private var _favoriteCheckLiveData =
        MutableLiveData<QumparanResource<CheckFavoriteResponse?>>()
    val favoriteCheckLiveData get() = _favoriteCheckLiveData

    private var _addToFavoriteLiveData =
        MutableLiveData<QumparanResource<SaveFavoriteResponse?>>()
    val addToFavoriteLiveData get() = _addToFavoriteLiveData

    private var _deleteFromFavoriteLiveData =
        MutableLiveData<QumparanResource<DeleteFavoriteResponse?>>()
    val deleteFromFavoriteLiveData get() = _deleteFromFavoriteLiveData

    private var _userFavoriteLiveData =
        MutableLiveData<QumparanResource<ListFavoriteBookResponse?>>()
    val userFavoriteLiveData get() = _userFavoriteLiveData

    private var _searchLiveData =
        MutableLiveData<QumparanResource<SearchBookResponse?>>()
    val searchLiveData get() = _searchLiveData

    fun fetchUserFavorite(id:String) = viewModelScope.launch {
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

    fun searchBook(term:String) = viewModelScope.launch {
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

    fun fetchDetailBook(id:String) = viewModelScope.launch {
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

    fun fetchIfFavorite(bookId:String,userId:String) = viewModelScope.launch {
        _favoriteCheckLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.checkIfFavorite(userId = userId,bookId)
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

    fun saveFavorite(bookId:String,userId:String) = viewModelScope.launch {
        _addToFavoriteLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.saveFavorite(userId = userId,bookId)
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

    fun deleteFromFavorite(bookId:String,userId:String) = viewModelScope.launch {
        _addToFavoriteLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.deleteFavorite(userId = userId,bookId)
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
}
