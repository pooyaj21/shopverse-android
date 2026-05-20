package com.shopverse.convention

import org.gradle.api.JavaVersion

internal object AndroidDefaults {
    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    const val MIN_SDK = 24
}

internal object JvmDefaults {
    val JVM_TARGET = JavaVersion.VERSION_17
}
