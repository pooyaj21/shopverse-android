package com.shopverse.convention.plugin

import com.shopverse.convention.JvmDefaults
import com.shopverse.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

@Suppress("unused") // Used in `build.gradle.kts`
class JvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }
            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JvmDefaults.JVM_TARGET
                targetCompatibility = JvmDefaults.JVM_TARGET
            }
            configureKotlin()
        }
    }
}
