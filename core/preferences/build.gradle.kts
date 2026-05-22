plugins {
    id("app.android.library")
    id("app.koin.android")
}

android {
    namespace = "com.shopverse.core.preferences"
}

dependencies {
    implementation(libs.androidx.security.crypto)
}
