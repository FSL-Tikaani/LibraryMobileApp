import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tikaan.libraryapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.tikaan.libraryapp"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.picasso)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.v262)
    implementation(libs.androidx.lifecycle.livedata.v262)
    implementation(libs.androidx.lifecycle.common.java8)

    implementation(libs.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}