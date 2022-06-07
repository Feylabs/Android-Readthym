package com.readthym.book.utils

import android.content.Context
import com.readthym.book.data.local.MyPreference

object AppUtils {

    fun logout(context: Context) {
        MyPreference(context).clearPreferences()
    }

}