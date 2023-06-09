package dev.zabolotskikh.authguard

import org.json.JSONArray
import org.json.JSONObject

internal fun JSONArray.toObjectList(): List<JSONObject> {
    val result = mutableListOf<JSONObject>()
    repeat(length()) {
        result += getJSONObject(it)
    }
    return result
}

internal inline fun <T : Any> JSONArray.mapNotNull(transform: (JSONObject) -> T): List<T> {
    return toObjectList().map(transform)
}