@file:Suppress("UnstableApiUsage")

import java.util.Properties

val SCREEN_ARGS = "screenArgs"

plugins {
    id("app.android.application")
    id("app.koin.android")
    id("kotlin-parcelize")
    alias(libs.plugins.androidx.navigation.safeargs)
}

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val supabaseUrl: String = localProps.getProperty("supabase.url")
val supabaseAnonKey: String = localProps.getProperty("supabase.anonKey")

android {
    namespace = "com.shopverse.android"
    defaultConfig {
        applicationId = "com.shopverse.android"
        versionCode = getVersionCode()
        versionName = "1.0.0"
        buildConfigField("String", "SCREEN_ARGS", "\"$SCREEN_ARGS\"")
        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
    }
    signingConfigs {
        create("release") {
            storeFile = file(localProps.getProperty("store"))
            storePassword = localProps.getProperty("storePass")
            keyAlias = localProps.getProperty("keyAlias")
            keyPassword = localProps.getProperty("keyPass")
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:service"))
    implementation(project(":core:preferences"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.android.material)
    implementation(libs.airbnb.lottie)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.recyclerview)
    // Drawable toolbox
    implementation(libs.drawabletoolbox)
    // Image loading
    implementation(libs.coil)
    // QR code generation
    implementation(libs.zxing.core)
    // RxJava 2 + RxAndroid
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
}

fun Project.getVersionCode(): Int {
    // Read version code without incrementing to avoid file I/O on every build
    // Use incrementVersionCode task to increment when needed
    val versionFile = file("version.properties")
    if (!versionFile.exists()) {
        return 1
    }
    val props = Properties().apply {
        load(versionFile.inputStream())
    }
    val oldCode = props.getProperty("VERSION_CODE").toIntOrNull() ?: 1
    val newCode = oldCode + 1
    props.setProperty("VERSION_CODE", newCode.toString())
    versionFile.writer().use {
        props.store(it, null)
    }
    return newCode
}