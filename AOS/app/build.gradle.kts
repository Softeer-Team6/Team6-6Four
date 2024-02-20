import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.softeer.team6four"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.softeer.team6four"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLIENT_ID", "\"${properties["CLIENT_ID"]}\"")
        buildConfigField(
            "String",
            "CLIENT_SECRET",
            "\"${properties["CLIENT_SECRET"]}\""
        )
        buildConfigField("String", "BASE_URL", "\"${properties["BASE_URL"]}\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["CLIENT_ID"] = properties["CLIENT_ID"] as String
            manifestPlaceholders["CLIENT_SECRET"] = properties["CLIENT_SECRET"] as String
        }
        release {
            isMinifyEnabled = false
            manifestPlaceholders["CLIENT_ID"] = properties["CLIENT_ID"] as String
            manifestPlaceholders["CLIENT_SECRET"] = properties["CLIENT_SECRET"] as String
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.naver.maps:map-sdk:3.17.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("io.coil-kt:coil:2.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}