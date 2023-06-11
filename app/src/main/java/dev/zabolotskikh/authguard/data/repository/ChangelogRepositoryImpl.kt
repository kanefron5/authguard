package dev.zabolotskikh.authguard.data.repository

import android.content.res.AssetManager
import dev.zabolotskikh.authguard.domain.model.ChangelogItem
import dev.zabolotskikh.authguard.domain.model.Release
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository
import dev.zabolotskikh.authguard.mapNotNull
import org.json.JSONArray
import javax.inject.Inject

private const val FILE_NAME = "changelog.json"
private const val EMPTY_ARRAY = "[]"

class ChangelogRepositoryImpl @Inject constructor(
    assets: AssetManager
) : ChangelogRepository {
    private val isExists: Boolean = assets.list("")?.contains(FILE_NAME) == true

    private val jsonString = if (!isExists) EMPTY_ARRAY else assets.open(FILE_NAME).use {
        try {
            it.readBytes().decodeToString()
        } catch (e: Exception) {
            EMPTY_ARRAY
        }
    }

    override fun get(): List<Release> {
        return try {
            JSONArray(jsonString).mapNotNull {
                val version = it.optString("version")
                val list = it.getJSONArray("changelog").mapNotNull { logItem ->
                    ChangelogItem(
                        logItem.getString("label"),
                        logItem.getString("text"),
                        logItem.getString("url"),
                    )
                }
                Release(version, list.toSet())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}