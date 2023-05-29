import java.util.Properties

val appCompileSdk: String by project
val appMinSdk: String by project
val passwordEncryptionSecret: String by rootProject.extra

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger)
}

android {
    namespace = "dev.zabolotskikh.passlock"
    compileSdk = appCompileSdk.toInt()

    defaultConfig {
        minSdk = appMinSdk.toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        all {
            var secret = passwordEncryptionSecret
            if (secret.isBlank()) {
                val passwordEncryptionSecretKey = "passwordEncryptionSecret"
                val properties = Properties().apply {
                    val file = project.file("$rootDir/encryption.properties")
                    if (file.exists()) load(file.inputStream())
                    else {
                        this[passwordEncryptionSecretKey] = ""
                    }
                }
                secret = properties.getProperty(passwordEncryptionSecretKey)
            }

            buildConfigField("String", "PASSWORD_SECRET", "\"$secret\"")
        }
        release {
            isMinifyEnabled = true
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
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.dagger.hilt.hilt)
    implementation(libs.dagger.hilt.work)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.dagger.hilt.androidx.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.testing)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.ui.tooling.tooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.hilt.navigation)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}