package com.readthym.book.di

import android.app.Activity
import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.Response
import com.readthym.book.data.local.MyPreference
import com.readthym.book.ui.SplashScreenActivity
import timber.log.Timber


class HttpCustomInterceptor(
    private val mySharedPreferences: MyPreference,
    private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val response = chain.proceed(req)
        when (response.code) {
            200->{
                Timber.d("intercept-manyunyu7 200 lanjutt")
            }
            401->{
               logout()
            }
            2201 -> {
                val newToken = updateToken(chain)
                Timber.d("updateTOKEN ${newToken}")
                Timber.d("intercept-manyunyu7 401 refresh with $newToken")
                Timber.d("newTOKEN MANYUNYU7 ${newToken}")
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", newToken)
                    .build()
                chain.proceed(newRequest)
            }
        }
        return response
    }

    private fun updateToken(chain: Interceptor.Chain): String {
        return ""
    }


    private fun logout() {
        MyPreference(context).clearPreferences()
        if (context is Activity) {
            context.finish()
        }
        val intent = Intent(context, SplashScreenActivity::class.java)
        intent.putExtra("message", "Sesi Anda Telah Habis, Silakan Login Kembali")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(intent)
    }


}