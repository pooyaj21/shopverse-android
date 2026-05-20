package com.shopverse.convention.plugin

import com.android.build.gradle.LibraryExtension
import com.shopverse.convention.AndroidDefaults
import com.shopverse.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused") // Used in `build.gradle.kts`
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidDefaults.TARGET_SDK
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }
        }
    }
}
