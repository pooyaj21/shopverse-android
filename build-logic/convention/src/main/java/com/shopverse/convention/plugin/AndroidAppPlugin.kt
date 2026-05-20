package com.shopverse.convention.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.shopverse.convention.AndroidDefaults
import com.shopverse.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused") // Used in `build.gradle.kts`
class AndroidAppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidDefaults.TARGET_SDK
            }
        }
    }
}
