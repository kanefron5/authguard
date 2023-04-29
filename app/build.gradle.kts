plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "dev.zabolotskikh.authentificator"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        applicationId = "dev.zabolotskikh.authentificator"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
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


    implementation(Dependencies.Kotlin.COROUTINES_CORE)
    implementation(Dependencies.Kotlin.COROUTINES_ANDROID)

    implementation(Dependencies.Androidx.CORE_KTX)
    implementation(Dependencies.Androidx.Lifecycle.RUNTIME_KTX)
    implementation(Dependencies.Androidx.Lifecycle.VIEWMODEL_KTX)
    implementation(Dependencies.Androidx.ACTIVITY_COMPOSE)

    implementation(platform(Dependencies.Androidx.Compose.COMPOSE_BOM))
    implementation(Dependencies.Androidx.Compose.UI)
    implementation(Dependencies.Androidx.Compose.UI_GRAPHICS)
    implementation(Dependencies.Androidx.Compose.UI_TOOLING_PREVIEW)
    implementation(Dependencies.Androidx.Compose.MATERIAL3)

    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.ESPRESSO_CORE)
    androidTestImplementation(Dependencies.Androidx.Compose.COMPOSE_BOM)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TEST)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TOOLING)
    androidTestImplementation(Dependencies.Androidx.Compose.UI_TEST_MANIFEST)
    androidTestImplementation(Dependencies.Androidx.Room.ROOM_TESTING)

}
