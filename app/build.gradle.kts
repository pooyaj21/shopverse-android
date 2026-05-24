import java.util.Properties

val SCREEN_ARGS = "screenArgs"

plugins {
    id("app.android.application")
    id("app.koin.android")
    id("kotlin-parcelize")
}

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val supabaseUrl: String =
    localProps.getProperty("supabase.url") ?: "https://cxfllbnxuvmeykjvtyeb.supabase.co"
val supabaseAnonKey: String = localProps.getProperty("supabase.anonKey") ?: ""

android {
    namespace = "com.shopverse.android"
    defaultConfig {
        applicationId = "com.shopverse.android"
        versionCode = 1
        versionName = "0.1.0"
        buildConfigField("String", "SCREEN_ARGS", "\"$SCREEN_ARGS\"")
        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
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
    implementation(libs.androidx.recyclerview)
    // Drawable toolbox
    implementation(libs.drawabletoolbox)
    // Image loading
    implementation(libs.coil)
    // RxJava 2 + RxAndroid
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
}
