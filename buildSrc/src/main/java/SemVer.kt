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
