plugins {
    id("app.android.application")
    id("app.koin.android")
}

android {
    namespace = "com.shopverse.android"
    defaultConfig {
        applicationId = "com.shopverse.android"
        versionCode = 1
        versionName = "0.1.0"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:service"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.android.material)
}
