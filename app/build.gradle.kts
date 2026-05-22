val SCREEN_ARGS = "screenArgs"

plugins {
    id("app.android.application")
    id("app.koin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.shopverse.android"
    defaultConfig {
        applicationId = "com.shopverse.android"
        versionCode = 1
        versionName = "0.1.0"
        buildConfigField("String", "SCREEN_ARGS", "\"$SCREEN_ARGS\"")
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
    implementation(project(":core:preferences"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.android.material)
    implementation(libs.airbnb.lottie)
    implementation(libs.androidx.navigation.fragment)
    // Drawable toolbox
    implementation(libs.drawabletoolbox)
}
