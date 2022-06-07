package com.readthym.book.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.book.data.AppRepository
import com.readthym.book.utils.Network
import kotlinx.coroutines.launch
import org.json.JSONObject

class SharedViewModel(val repo: AppRepository) : ViewModel() {
    val currentMenu = MutableLiveData(BottomMenu.DEFAULT)

}

enum class BottomMenu(name: String, code: String) {
    DEFAULT("SD", "0"),
    HOME("SD", "1"),
    NOTIF("SD", "2"),
    PROFILE("SD", "3"),
    DOC("SD", "4")
}