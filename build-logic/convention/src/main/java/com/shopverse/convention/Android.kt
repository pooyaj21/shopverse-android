package com.shopverse.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = AndroidDefaults.COMPILE_SDK

        defaultConfig {
            minSdk = AndroidDefaults.MIN_SDK
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            create("stage") {
                initWith(getByName("release"))
                buildConfigField("boolean", "DEBUG", "true")
            }
        }

        packaging {
            resources {
                excludes += "META-INF/*"
            }
        }

        compileOptions {
            sourceCompatibility = JvmDefaults.JVM_TARGET
            targetCompatibility = JvmDefaults.JVM_TARGET
            isCoreLibraryDesugaringEnabled = true
        }
        buildFeatures {
            buildConfig = true
        }
    }

    configureKotlin()

    dependencies {
        add("implementation", project(":core:shared"))
        add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())

        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}
