import java.io.FileInputStream
import java.util.Properties

buildscript {
    dependencies {
        classpath(libs.dagger.hilt.plugin)
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.dagger) apply false
}

ext {
    data class SemVer(
        val major: Int,
        val minor: Int,
        val patch: Int,
    ) {
        constructor(major: String, minor: String, patch: String) : this(
            major = major.toInt(),
            minor = minor.toInt(),
            patch = patch.toInt(),
        )

        val versionCode: Int = major * 1_000_000 + minor * 1_000 + patch
        val versionName: String = "$major.$minor.$patch"

        init {
            require(major in 0..999) { "The greatest value major version is 999" }
            require(minor in 0..999) { "The greatest value minor version is 999" }
            require(patch in 0..999) { "The greatest value patch version is 999" }
        }
    }

    fun getProperty(file: String, name: String): String {
        try {
            FileInputStream(file).use {
                val prop = Properties().apply { load(it) }
                return prop.getProperty(name)
            }
        } catch (e: Exception) {
            return ""
        }
    }

    fun currentVersion() = SemVer(
        getProperty("gradle.properties", "app_version_major"),
        getProperty("gradle.properties", "app_version_minor"),
        getProperty("gradle.properties", "app_version_patch")
    )

    extra.apply {
        set("appVersionName", currentVersion().versionName)
        set("appVersionCode", currentVersion().versionCode)
    }
}