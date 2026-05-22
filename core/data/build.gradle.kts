plugins {
    id("app.android.library")
    id("app.koin.android")
}

android {
    namespace = "com.shopverse.core.data"
}

dependencies {
    implementation(project(":core:service"))
    implementation(project(":core:model"))
    implementation(project(":core:preferences"))
}
