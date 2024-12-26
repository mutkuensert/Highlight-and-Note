import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.compose.compiler.plugin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mutkuensert.highlightandnote"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mutkuensert.highlightandnote"
        minSdk = 23
        targetSdk = 35
        versionCode = 7
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["KEY_PASSWORD"] as String
            storeFile = file(keystoreProperties["STORE_FILE"] as String)
            storePassword = keystoreProperties["STORE_PASSWORD"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.hilt.navigation)
    implementation(libs.compose.navigation)
    implementation(libs.compose.view.model)

    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.dagger.hilt)
    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.navigation.ui)

    implementation(libs.kotlin.serialization)

    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}