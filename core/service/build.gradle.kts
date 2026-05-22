plugins {
    id("app.android.library")
    id("app.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.shopverse.core.service"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
