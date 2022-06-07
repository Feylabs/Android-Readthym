package com.readthym.book.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.readthym.book.R


object UIHelper {

    fun showLongToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

    fun View.makeGone(){
        this.visibility=View.GONE
    }
    fun View.makeVisible(){
        this.visibility=View.VISIBLE
    }

    fun ImageView.loadImageFromURL(
        context: Context,
        url: String? = "",
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(url)
            .placeholder(thumbnailsType.value)
//            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    fun ImageView.loadImage(
        context: Context,
        drawable: Int,
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(drawable)
            .placeholder(thumbnailsType.value)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    enum class ThumbnailsType(val value: Int) {
        ADD_PHOTO_1(R.drawable.placeholder_book),
        LOADING_1(R.drawable.placeholder_book),
    }

    fun String.renderHtmlToString(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return (Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)).toString()
        } else {
            return (Html.fromHtml(this)).toString()
        }
    }

    fun Fragment.hideActionBar() {
        activity?.actionBar?.hide()
        (getActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    fun Fragment.showActionBar() {
        activity?.actionBar?.show()
        (getActivity() as AppCompatActivity).supportActionBar?.hide()
    }


}