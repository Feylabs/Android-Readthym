package com.readthym.book.utils.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun showToast(text: String, isLong: Boolean = false) {
        var duration = Toast.LENGTH_LONG
        if (!isLong)
            duration = Toast.LENGTH_SHORT
        Toast.makeText(this, text, duration).show()
    }

    fun hideSoftKeyBoard(context: Context) {
        val myCurrentFocusView: View? = currentFocus
        myCurrentFocusView?.postDelayed(Runnable { // TODO Auto-generated method stub
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(myCurrentFocusView.windowToken, 0)
        }, 100)
    }

}