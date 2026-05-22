package com.shopverse.core.shared

sealed class AppResult<out T> {

    data class Success<T>(val value: T) : AppResult<T>()

    sealed class Error(
        open val message: String?,
        open val cause: Throwable?
    ) : AppResult<Nothing>() {

        data class Local(
            override val message: String?,
            override val cause: Throwable?
        ) : Error(message, cause)

        data class Remote(
            val httpCode: Int,
            override val message: String?,
            override val cause: Throwable?
        ) : Error(message, cause)
    }
}

@Suppress("unused")
inline val AppResult<*>.isSuccess: Boolean
    get() = this is AppResult.Success<*>

@Suppress("unused")
inline val AppResult<*>.isError: Boolean
    get() = this is AppResult.Error

@Suppress("unused")
inline fun <T> AppResult<T>.doOnSuccess(onSuccess: (value: T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) onSuccess(value)
    return this
}

@Suppress("unused")
inline fun <T> AppResult<T>.doOnError(
    onError: (httpCode: Int?, message: String?, cause: Throwable?) -> Unit
): AppResult<T> {
    when (this) {
        is AppResult.Error.Local -> onError(null, message, cause)
        is AppResult.Error.Remote -> onError(httpCode, message, cause)
        else -> Unit
    }
    return this
}

@Suppress("unused")
inline fun <R, T> AppResult<T>.fold(
    onSuccess: (value: T) -> R,
    onError: (httpCode: Int?, message: String?, cause: Throwable?) -> R
): R = when (this) {
    is AppResult.Success -> onSuccess(value)
    is AppResult.Error.Local -> onError(null, message, cause)
    is AppResult.Error.Remote -> onError(httpCode, message, cause)
}

@Suppress("unused")
inline fun <R, T> AppResult<T>.mapIfSuccess(transform: (value: T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(value))
    is AppResult.Error.Local -> AppResult.Error.Local(message, cause)
    is AppResult.Error.Remote -> AppResult.Error.Remote(httpCode, message, cause)
}

@Suppress("unused")
fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.value

@Suppress("unused")
fun <R, T : R> AppResult<T>.getOrDefault(defaultValue: R): R = when (this) {
    is AppResult.Success -> value
    is AppResult.Error -> defaultValue
}
