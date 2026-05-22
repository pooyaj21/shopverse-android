package com.shopverse.core.shared

fun interface BaseUrlChangeCallback {
    fun onBaseUrlChanged(baseUrl: String)
}

class EnvironmentConfiguration private constructor(
    private val _baseUrl: String,
    private val _languageIdentifier: String,
    val applicationName: String,
    val appVersionCode: Int,
    val appVersionName: String,
    val isInDebugMode: Boolean,
    val androidSdkVersion: Int
) {

    var baseUrl: String = _baseUrl
        private set

    var languageIdentifier: String = _languageIdentifier
        private set

    private val baseUrlChangeCallbacks = mutableSetOf<BaseUrlChangeCallback>()

    fun changeLanguageIdentifier(languageIdentifier: String) {
        this.languageIdentifier = languageIdentifier
    }

    fun changeBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
        baseUrlChangeCallbacks.forEach { it.onBaseUrlChanged(baseUrl) }
    }

    fun addBaseUrlChangeCallback(callback: BaseUrlChangeCallback) {
        baseUrlChangeCallbacks.add(callback)
    }

    fun removeBaseUrlChangeCallback(callback: BaseUrlChangeCallback) {
        baseUrlChangeCallbacks.remove(callback)
    }

    companion object {

        private var instance: EnvironmentConfiguration? = null

        fun init(
            baseUrl: String,
            languageIdentifier: String,
            applicationName: String,
            appVersionCode: Int,
            appVersionName: String,
            isInDebugMode: Boolean,
            androidSdkVersion: Int
        ) = synchronized(this) {
            check(instance == null) { "EnvironmentConfiguration is already initialized!" }
            instance = EnvironmentConfiguration(
                _baseUrl = baseUrl,
                _languageIdentifier = languageIdentifier,
                applicationName = applicationName,
                appVersionCode = appVersionCode,
                appVersionName = appVersionName,
                isInDebugMode = isInDebugMode,
                androidSdkVersion = androidSdkVersion
            )
        }

        fun getInstance(): EnvironmentConfiguration =
            checkNotNull(instance) { "EnvironmentConfiguration is not initialized!" }

        fun resetForTest() {
            instance = null
        }

        val isInitialized: Boolean get() = instance != null
    }
}
