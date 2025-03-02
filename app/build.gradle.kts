plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")

}

android {
    namespace = "com.mohit.healthy_aahar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mohit.healthy_aahar"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //retrofit and gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //room database
//    // Room Database
//    implementation("androidx.room:room-runtime:2.5.2")
//    kapt("androidx.room:room-compiler:2.5.2")
//    implementation("androidx.room:room-ktx:2.5.2")
    // Dagger-Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

// Hilt for Jetpack Compose Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // icons
    implementation("androidx.compose.material:material-icons-extended:1.7.7")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.3")) // ✅ Firebase BOM (Auto Manages Versions)
    implementation("com.google.firebase:firebase-auth-ktx") // ✅ Firebase Auth Library
    implementation("com.google.android.gms:play-services-auth:20.7.0") // ✅ Google Sign-In

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("org.tensorflow:tensorflow-lite:2.5.0")

    // Accompanist for Compose (for Scrollable TabRow if needed)
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // Jetpack Compose Charts Library (Bar Charts, Line Charts)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // Circular Progress & Foundation
    implementation ("androidx.compose.foundation:foundation:1.5.0")

    // KotlinX Coroutines (for Data Updates in Graphs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")



}
kapt{
    correctErrorTypes = true
}