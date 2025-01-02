import java.util.Properties
import java.io.FileInputStream

val keysProperties = Properties()
val keysFile = rootProject.file("keys.properties")

if (keysFile.exists()) {
    keysProperties.load(FileInputStream(keysFile))
} else {
println("keys.properties file not found!")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.spaceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.spaceapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Add BuildConfig field for API_KEY
        buildConfigField("String", "API_KEY", "\"Bearer ${keysProperties["apiKey"]}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey = keysProperties["apiKey"] ?: "default_value"
        println("Loaded API Key: $apiKey") // Debug line
        buildConfigField("String", "API_KEY", "\"Bearer $apiKey\"")

        println("keysFile absolute path: ${keysFile.absolutePath}")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.firebase:firebase-auth:22.1.1")
}
