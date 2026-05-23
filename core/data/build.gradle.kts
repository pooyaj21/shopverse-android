plugins {
    id("app.android.library")
    id("app.koin.android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.shopverse.core.data"
}

dependencies {
    implementation(project(":core:service"))
    implementation(project(":core:model"))
    implementation(project(":core:preferences"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
