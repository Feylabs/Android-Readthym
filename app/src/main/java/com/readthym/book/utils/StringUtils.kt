package com.readthym.book.utils

object StringUtils {
    fun String.splitCamelCase(): String {
        return this.replace(
            String.format(
                "%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"
            ).toRegex(),
            ","
        )
    }

}