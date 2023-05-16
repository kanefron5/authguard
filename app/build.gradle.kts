plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "dev.zabolotskikh.authguard"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        applicationId = "dev.zabolotskikh.authguard"
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = currentVersion().versionCode
        versionName = currentVersion().versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(Dependencies.Androidx.Room.ROOM_KTX)
    kapt(Dependencies.Androidx.Room.ROOM_COMPILER)

    implementation(Dependencies.Dagger.HILT_ANDROID)
    kapt(Dependencies.Dagger.HILT_ANDROID_COMPILER)
    kapt(Dependencies.Androidx.Hilt.COMPILER)
    implementation(Dependencies.Androidx.Hilt.NAVIGATION_COMPOSE)

    implementation(Dependencies.Kotlin.COROUTINES_CORE)
    implementation(Dependencies.Kotlin.COROUTINES_ANDROID)

    implementation(Dependencies.Androidx.CORE_KTX)
    implementation(Dependencies.Androidx.Lifecycle.RUNTIME_KTX)
    implementation(Dependencies.Androidx.Lifecycle.VIEWMODEL_KTX)
    implementation(Dependencies.Androidx.Lifecycle.LIVEDATA_KTX)
    implementation(Dependencies.Androidx.ACTIVITY_COMPOSE)

    implementation(Dependencies.Androidx.Camera.CAMERA2)
    implementation(Dependencies.Androidx.Camera.CAMERA_VIEW)
    implementation(Dependencies.Androidx.Camera.LIFECYCLE)

    implementation(platform(Dependencies.Androidx.Compose.COMPOSE_BOM))
    implementation(Dependencies.Androidx.Compose.UI)
    implementation(Dependencies.Androidx.Compose.UI_GRAPHICS)
    implementation(Dependencies.Androidx.Compose.ICONS)
    implementation(Dependencies.Androidx.Compose.UI_TOOLING_PREVIEW)
    implementation(Dependencies.Androidx.Compose.MATERIAL3)
    implementation(Dependencies.Androidx.Compose.NAVIGATION_COMPOSE)

    implementation(Dependencies.APACHE_COMMONS_CODEC)
    implementation(Dependencies.COMPOSE_SETTINGS)
    implementation(Dependencies.BARCODE_SCANNING)

    implementation(platform(Dependencies.Firebase.FIREBASE_BOM))
    implementation(Dependencies.Firebase.ANALYTICS)
    implementation(Dependencies.Firebase.AUTH)
    implementation(Dependencies.Firebase.CRASHLYTICS)

    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.ESPRESSO_CORE)
    androidTestImplementation(Dependencies.Androidx.Compose.COMPOSE_BOM)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TEST)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TOOLING)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TEST_MANIFEST)
    androidTestImplementation(Dependencies.Androidx.Room.ROOM_TESTING)

}
