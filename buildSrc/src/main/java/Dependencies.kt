object Dependencies {

    const val APACHE_COMMONS_CODEC = "commons-codec:commons-codec:1.15"
    const val COMPOSE_SETTINGS = "com.github.alorma:compose-settings-ui-m3:0.26.0"

    object Dagger {
        private const val version = "2.44"
        const val HILT_ANDROID = "com.google.dagger:hilt-android:$version"
        const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:$version"
    }

    // https://github.com/Kotlin/kotlinx.coroutines
    object Kotlin {
        private const val version = "1.6.4"
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Androidx {
        // https://developer.android.com/jetpack/androidx/releases/core
        const val CORE_KTX = "androidx.core:core-ktx:1.10.0"

        // https://developer.android.com/jetpack/androidx/releases/activity
        const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose:1.7.1"

        // https://developer.android.com/jetpack/androidx/releases/lifecycle
        object Lifecycle {
            private const val version = "2.6.1"

            const val RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        }

        // https://developer.android.com/jetpack/androidx/releases/compose
        object Compose {
            const val COMPOSE_BOM = "androidx.compose:compose-bom:2023.05.00"
            const val UI = "androidx.compose.ui:ui"
            const val UI_GRAPHICS = "androidx.compose.ui:ui-graphics"
            const val ICONS = "androidx.compose.material:material-icons-extended"
            const val UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"
            const val UI_TOOLING = "androidx.compose.ui:ui-tooling"
            const val UI_TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest"
            const val MATERIAL3 = "androidx.compose.material3:material3:1.1.0-rc01"
            const val UI_TEST = "androidx.compose.ui:ui-test-junit4"
            const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:2.5.3"
        }

        // https://developer.android.com/jetpack/androidx/releases/room
        object Room {
            private const val version = "2.5.1"
            const val ROOM_KTX = "androidx.room:room-ktx:$version"
            const val ROOM_COMPILER = "androidx.room:room-compiler:$version"
            const val ROOM_TESTING = "androidx.room:room-testing:$version"
        }

        // https://developer.android.com/jetpack/androidx/releases/hilt
        object Hilt {
            private const val version = "1.0.0"
            const val LIFECYCLE_VIEWMODEL = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
            const val COMPILER = "androidx.hilt:hilt-compiler:$version"
            const val NAVIGATION_COMPOSE = "androidx.hilt:hilt-navigation-compose:$version"
        }
    }

    object Test {
        // https://mvnrepository.com/artifact/junit/junit
        const val JUNIT = "junit:junit:4.13.2"
    }

    object AndroidTest {
        // https://mvnrepository.com/artifact/androidx.test.ext/junit
        const val JUNIT = "androidx.test.ext:junit:1.1.3"

        // https://mvnrepository.com/artifact/androidx.test.espresso/espresso-core
        private const val espressoVersion = "3.5.0"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:$espressoVersion"
    }
}