plugins {
    id("app.android.library")
    id("app.koin.android")
}

android {
    namespace = "com.shopverse.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
}
