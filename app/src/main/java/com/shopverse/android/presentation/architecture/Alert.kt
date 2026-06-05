package com.shopverse.android.presentation.architecture

sealed class Alert {
    abstract val message: String
    data class Success(override val message: String) : Alert()
    data class Warning(override val message: String) : Alert()
    data class Info(override val message: String) : Alert()
    data class Error(override val message: String) : Alert()
}
