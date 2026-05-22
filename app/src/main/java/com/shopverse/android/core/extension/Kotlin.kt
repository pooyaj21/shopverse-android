package com.shopverse.android.core.extension

inline fun <T> tryOptional(expression: () -> T): T? {
    return try {
        expression()
    } catch (ex: Throwable) {
        null
    }
}
