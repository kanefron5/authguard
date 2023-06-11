package dev.zabolotskikh

import org.gradle.api.Action
import org.gradle.api.Project

open class ChangelogPluginExtension {
    open var repositoryOwner: String = ""
    open var repositoryName: String = ""
    open var filePath: String = ""
    open var accessToken: String? = null
}

fun Project.changelog(configure: Action<ChangelogPluginExtension>) {
    configure.execute(extensions.create("changelog", ChangelogPluginExtension::class.java))
}
