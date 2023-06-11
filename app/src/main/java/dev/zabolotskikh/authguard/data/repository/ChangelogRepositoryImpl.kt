package dev.zabolotskikh.authguard.data.repository

import android.content.res.AssetManager
import dev.zabolotskikh.authguard.BuildConfig.CHANGELOG_FILE_NAME
import dev.zabolotskikh.authguard.domain.model.ChangelogItem
import dev.zabolotskikh.authguard.domain.model.Release
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository
import dev.zabolotskikh.authguard.mapNotNull
import org.json.JSONArray
import javax.inject.Inject

private const val EMPTY_ARRAY = "[]"

class ChangelogRepositoryImpl @Inject constructor(
    private val assets: AssetManager
) : ChangelogRepository {
    private val isExists = assets.list("")?.contains(CHANGELOG_FILE_NAME) == true

    private val jsonString: String
        get() {
            if (!isExists) return EMPTY_ARRAY
            assets.open(CHANGELOG_FILE_NAME).use {
                try {
                    return it.readBytes().decodeToString()
                } catch (e: Exception) {
                    return EMPTY_ARRAY
                }
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
            emptyList()
        }
    }
}