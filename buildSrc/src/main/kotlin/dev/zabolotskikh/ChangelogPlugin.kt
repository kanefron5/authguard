package dev.zabolotskikh

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

private const val GITHUB_API_URL = "https://api.github.com"
private const val GITHUB_URL = "https://github.com"

class ChangelogPlugin : Plugin<Project> {
    private lateinit var rootDir: File
    private val gson = Gson()

    override fun apply(project: Project) {
        rootDir = project.rootDir

        println("Plugin ${this.javaClass.simpleName} applied on ${project.name}")
        project.tasks.register("generateChangelog") {
            doLast {
                val extension =
                    project.extensions.findByName("changelog") as? ChangelogPluginExtension

                require(extension != null) { "Please configure plugin with `changelog { ... }`" }
                require(extension.repositoryName.isNotBlank()) { "Please specify repositoryName" }
                require(extension.repositoryOwner.isNotBlank()) { "Please specify repositoryOwner" }
                require(extension.filePath.isNotBlank()) { "Please specify filePath" }

                println("Hello Gradle! Name: ${extension.repositoryName}")

                val allTags = "git tag --sort=-committerdate".execute().trim().lines()
                val releases = mutableListOf<Release>()
                for ((index, current) in allTags.withIndex()) {
                    releases += Release(
                        current,
                        getChangelog(
                            owner = extension.repositoryOwner,
                            name = extension.repositoryName,
                            accessToken = extension.accessToken,
                            currentTag = current,
                            previousTag = allTags.getOrNull(index + 1)
                        )
                    )
                }
                File(extension.filePath).apply {
                    writeText(gson.toJson(releases))
                }
            }
        }
    }


    private fun String.execute(workingDir: File = rootDir): String {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray()).directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(1, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText().trim()
    }

    private fun getChangelog(
        owner: String,
        name: String,
        accessToken: String?,
        currentTag: String,
        previousTag: String?
    ): Set<ChangelogItem> {
        var cmd = "git log --oneline --merges --date-order --pretty=\"%s\" $currentTag"
        if (previousTag != null) cmd += "...$previousTag"
        return cmd.execute().trim().lines().filter { checkIsCommitCloseIssue(it) }
            .mapNotNull { line ->
                getIssueId(line)?.let { issueId ->
                    val url = URL("$GITHUB_API_URL/repos/$owner/$name/issues/$issueId")
                    val connection = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        accessToken?.apply { setRequestProperty("Authorization", "Bearer $this") }
                    }

                    val response = connection.inputStream.use { it.readBytes().decodeToString() }
                    connection.disconnect()


                    val parseJson = gson.fromJson(response, JsonObject::class.java)

                    val title = parseJson.get("title").asString
                    ChangelogItem(
                        "ISSUE-$issueId",
                        title,
                        "$GITHUB_URL/$owner/$name/issues/$issueId"
                    )
                }
            }.toSet()
    }

    private val regex = Regex("close-#\\d+|\$")
    private fun checkIsCommitCloseIssue(commitText: String) = regex.find(commitText) != null
    private fun getIssueId(commitText: String) =
        regex.find(commitText)?.value?.substringAfter("close-#")?.toIntOrNull()
}

