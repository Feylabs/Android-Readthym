package com.readthym.book.ui.rythm_home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.book.data.remote.RemoteDataSource
import com.readthym.book.data.remote.reqres.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class RythmHomeViewModel(val ds: RemoteDataSource) : ViewModel() {

    private var _booksLiveData =
        MutableLiveData<QumparanResource<ReadthymAllBookResponse?>>()
    val booksLiveData get() = _booksLiveData

    fun getBooks() = viewModelScope.launch {
        _booksLiveData.postValue(QumparanResource.Loading())
        try {
            val res = ds.getBooks()
            if (res.isSuccessful) {
                _booksLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                var message = res.message().toString()
                res.errorBody()?.let {
                    val jsonObj = JSONObject(it.charStream().readText())
                    message = jsonObj.getString("message")
                }
                _booksLiveData.postValue(QumparanResource.Error(message))
            }
        } catch (e: Exception) {
            _booksLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }
}