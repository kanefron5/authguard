
fun currentVersion(): SemVer {
    val major: String = Config.getProperty("gradle.properties", "app_version_major")
    val minor: String = Config.getProperty("gradle.properties", "app_version_minor")
    val patch: String = Config.getProperty("gradle.properties", "app_version_patch")
    return SemVer(major, minor, patch)
}

fun String.field() = "\"${this}\""
fun Int.field() = "$this"
fun Boolean.field() = "$this"

