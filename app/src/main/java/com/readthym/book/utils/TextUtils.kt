package com.readthym.book.utils

import android.text.Html

import android.text.Spanned
import java.lang.StringBuilder
import android.os.Build





class TextUtils {
    object BulletListBuilder {
        private const val SPACE = " "
        private const val BULLET_SYMBOL = "&#8226"
        private val EOL = System.getProperty("line.separator")
        private const val TAB = "\t"
        fun getBulletList(header: String? = null, items: MutableList<String>): String {
            val listBuilder = StringBuilder()
            if (header != null && !header.isEmpty()) {
                listBuilder.append(header + EOL + EOL)
            }
            if (items != null && items.size != 0) {
                for (item in items) {
                    val formattedItem = fromHtml(BULLET_SYMBOL+ SPACE+item)
                    listBuilder.append(TAB + formattedItem + EOL)
                }
            }
            return listBuilder.toString()
        }

        private fun fromHtml(source: String?): Spanned? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(source)
            }
        }
    }
}