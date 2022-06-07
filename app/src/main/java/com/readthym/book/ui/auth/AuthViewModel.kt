package com.readthym.book.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.book.data.remote.RemoteDataSource
import com.readthym.book.data.remote.reqres.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


class AuthViewModel(
    val repo: RemoteDataSource
) : ViewModel() {

    private var _loginLiveData = MutableLiveData<QumparanResource<ReadthymLoginResponse?>>()
    val loginLiveData get() = _loginLiveData

    private var _registerLiveData = MutableLiveData<QumparanResource<RegisterResponse?>>()
    val registerLiveData get() = _registerLiveData

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginLiveData.postValue(QumparanResource.Loading())
            try {
                val res = repo.login(email, password)
                Timber.d("public info response $res")
                if (res.isSuccessful) {
                    _loginLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _loginLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun register(name:String, email: String, password: String) {
        viewModelScope.launch {
            _registerLiveData.postValue(QumparanResource.Loading())
            try {
                val res = repo.register(name,email, password)
                if (res.isSuccessful) {
                    _registerLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _registerLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _registerLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}