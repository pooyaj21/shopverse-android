plugins {
    `kotlin-dsl`
}

group = "com.shopverse.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("jvmLibrary") {
            id = "app.jvm.library"
            implementationClass = "com.shopverse.convention.plugin.JvmLibraryPlugin"
        }
        register("androidLibrary") {
            id = "app.android.library"
            implementationClass = "com.shopverse.convention.plugin.AndroidLibraryPlugin"
        }
        register("androidApplication") {
            id = "app.android.application"
            implementationClass = "com.shopverse.convention.plugin.AndroidAppPlugin"
        }
        register("koin") {
            id = "app.koin"
            implementationClass = "com.shopverse.convention.plugin.KoinPlugin"
        }
        register("koinAndroid") {
            id = "app.koin.android"
            implementationClass = "com.shopverse.convention.plugin.KoinAndroidPlugin"
        }
    }
}
