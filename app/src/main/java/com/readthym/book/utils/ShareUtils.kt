package com.readthym.book.utils

import android.app.Activity
import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.ClipboardManager
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import android.widget.Toast
import com.readthym.book.R
import java.lang.Exception

object ShareUtils {

    const val SHARE_FB = "com.facebook.katana"
    const val SHARE_TWITTER = "com.twitter.android"
    const val SHARE_TELE = "org.telegram.messenger"
    const val SHARE_WA = "com.whatsapp"
    const val SHARE_LINK = "LINK"

    fun sharingToSocialMedia(
        context: Activity,
        application: String,
        title: String,
    ) {

        when (application) {
            SHARE_TWITTER -> {
                shareTwitter(context, title)
            }
            SHARE_FB -> {
                shareFacebook(context, title)
            }
            SHARE_WA -> {
                shareWhatsapp(context, title, "")
            }
            SHARE_LINK -> {
                setClipboard(context, title)
                Toast.makeText(
                    context.applicationContext,
                    context.getString(R.string.Link_copied),
                    Toast.LENGTH_LONG
                ).show()
            }
            SHARE_TELE -> {
                shareTelegram(context, title)
            }
        }
    }


    private fun checkAppInstall(context: Context, uri: String): Boolean {
        val pm: PackageManager = context.packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    /**
     * Share on Facebook. Using Facebook app if installed or web link otherwise.
     *
     * @param activity activity which launches the intent
     * @param text     not used/allowed on Facebook
     * @param url      url to share
     */
    fun shareFacebook(activity: Activity, text: String?) {
        var facebookAppFound = false
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        val pm = activity.packageManager
        val activityList = pm.queryIntentActivities(shareIntent, 0)
        for (app in activityList) {
            if (app.activityInfo.packageName.contains("com.facebook.katana")) {
                val activityInfo = app.activityInfo
                val name =
                    ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                shareIntent.component = name
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$text"
            shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }
        activity.startActivity(shareIntent)
    }


    fun shareTwitter(
        activity: Activity,
        text: String,
    ) {
        val tweetUrl = StringBuilder("https://twitter.com/intent/tweet?text=")
        tweetUrl.append(if (TextUtils.isEmpty(text)) urlEncode(" ") else urlEncode(text))
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl.toString()))
        val matches = activity.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName)
            }
        }
        activity.startActivity(intent)
    }

    /**
     * Share on Whatsapp (if installed)
     *
     * @param activity activity which launches the intent
     * @param text     text to share
     * @param url      url to share
     */
    fun shareWhatsapp(activity: Activity, text: String, url: String) {
        val pm = activity.packageManager
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp")
            waIntent.putExtra(Intent.EXTRA_TEXT, "$text $url")
            activity.startActivity(
                Intent.createChooser(waIntent, activity.getString(R.string.Share))
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(
                activity, activity.getString(R.string.Share),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun shareTelegram(activity: Context, msg: String) {
        val appName = "org.telegram.messenger"
        val mContext = activity
        val isAppInstalled: Boolean = isAppAvailable(activity, appName)
        if (isAppInstalled) {
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            myIntent.type = "text/plain"
            myIntent.setPackage(appName)
            myIntent.putExtra(Intent.EXTRA_TEXT, "$msg ")
            mContext.startActivity(Intent.createChooser(myIntent, "Share with"))
        } else {
            Toast.makeText(mContext, "Telegram Not Installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun isAppAvailable(context: Context, appName: String?): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
        } else {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }
    }

    /**
     * Convert to UTF-8 text to put it on url format
     *
     * @param s text to be converted
     * @return text on UTF-8 format
     */
    fun urlEncode(s: String): String? {
        return try {
            URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("URLEncoder.encode() failed for $s")
        }
    }
}