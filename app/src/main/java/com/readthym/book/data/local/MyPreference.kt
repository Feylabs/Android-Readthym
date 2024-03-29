package com.readthym.book.data.local

import android.content.Context
import android.content.SharedPreferences

class MyPreference(context: Context) {

    private val PREFS_NAME = "kotlinpref"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun save(KEY_NAME: String, value: Int) {
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String) {
        editor.putString(KEY_NAME, value)
        editor.commit()
    }

    fun saveTokenWithTemplate(TOKEN: String) {
        editor.putString("TOKEN", "Bearer $TOKEN")
        editor.commit()
    }

    fun getToken(): String {
        return sharedPref.getString("TOKEN", "").orEmpty()
    }

    fun getRole(): String? {
        return sharedPref.getString("ROLE", "")
    }

    fun getTokenRaw(): String? {
        return sharedPref.getString("TOKEN_RAW", "")
    }

    fun getUserID(): String? {
        return sharedPref.getString("USER_ID", "")
    }

    fun saveUserID(id: String) {
        editor.putString("USER_ID", id)
        editor.commit()
    }

    fun saveUserEmail(id: String) {
        editor.putString("USER_EMAIL", id)
        editor.commit()
    }

    fun getUserEmail(): String? {
        return sharedPref.getString("USER_EMAIL", "")
    }

    fun saveUserPassword(id: String) {
        editor.putString("USER_PASSWORD", id)
        editor.commit()
    }



    fun getUserPassword(): String? {
        return sharedPref.getString("USER_PASSWORD", "")
    }

    fun save(KEY_NAME: String, value: Boolean) {
        editor.putBoolean(KEY_NAME, value)
        editor.commit()
    }

    fun getPrefBool(KEY_NAME: String): Boolean? {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun getPrefString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun clearPreferences() {
        editor.clear()
        editor.commit()
    }

    fun getUserName(): String {
        return sharedPref.getString("NAME", "").orEmpty()
    }
    fun getUserRole(): String {
        return sharedPref.getString("ROLE", "").orEmpty()
    }

    fun saveUserName(name: String) {
        editor.putString("NAME", name)
        editor.commit()
    }

    fun saveUserRole(name: String) {
        editor.putString("ROLE", name)
        editor.commit()
    }


}