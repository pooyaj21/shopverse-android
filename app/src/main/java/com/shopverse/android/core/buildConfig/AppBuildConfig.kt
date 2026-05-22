package com.shopverse.android.core.buildConfig

import android.os.Build
import com.shopverse.android.BuildConfig

interface AppBuildConfig {
    val applicationId: String
    val isDebug: Boolean
    val versionName: String
    val versionCode: Int
    val sdkInt: Int
    val deviceModel: String
    val deviceManufacturer: String
}

class RealAppBuildConfig : AppBuildConfig {

    override val applicationId = BuildConfig.APPLICATION_ID
    override val isDebug = BuildConfig.DEBUG
    override val versionName = BuildConfig.VERSION_NAME
    override val versionCode = BuildConfig.VERSION_CODE
    override val sdkInt = Build.VERSION.SDK_INT
    override val deviceModel = Build.MODEL
    override val deviceManufacturer = Build.MANUFACTURER

}