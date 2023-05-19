import java.util.Properties

val signingKeyAlias: String by project
val signingKeyPassword: String by project
val signingStorePassword: String by project
val appCompileSdk: String by project
val appMinSdk: String by project
val appTargetSdk: String by project
val appVersionName: String by rootProject.extra
val appVersionCode: Int by rootProject.extra

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger)
}

android {
    namespace = "dev.zabolotskikh.authguard"
    compileSdk = appCompileSdk.toInt()

    defaultConfig {
        applicationId = "dev.zabolotskikh.authguard"
        minSdk = appMinSdk.toInt()
        targetSdk = appTargetSdk.toInt()
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        this.create("config") {
            if (signingKeyAlias.isBlank() && signingKeyPassword.isBlank() && signingStorePassword.isBlank()) {
                val propertiesFile = project.file("../signing.properties")
                val properties = Properties()
                properties.load(propertiesFile.inputStream())

                val signingKeyAlias = properties.getProperty("signingKeyAlias")
                val signingKeyPassword = properties.getProperty("signingKeyPassword")
                val signingStorePassword = properties.getProperty("signingStorePassword")

                keyAlias = signingKeyAlias
                keyPassword = signingKeyPassword
                storePassword = signingStorePassword
            } else {
                keyAlias = signingKeyAlias
                keyPassword = signingKeyPassword
                storePassword = signingStorePassword
            }

            storeFile = file("../keystore.jks")
        }
    }

    buildTypes {
        all {
            signingConfig = signingConfigs.getByName("config")
        }

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
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation(libs.dagger.hilt.hilt)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.dagger.hilt.androidx.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.graphics)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.ui.tooling.tooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.androidx.compose.settings)

    implementation(libs.apache.commons.codec)
    implementation(libs.google.barcode.scanning)
    implementation(libs.google.accompanist.permissions)


    testImplementation(libs.junit)
}
