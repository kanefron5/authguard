package dev.zabolotskikh.passlock

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class PreferenceDelegate<T>(
    private val key: Preferences.Key<T>
) : ReadWriteProperty<Preferences, T?> {
    override operator fun getValue(thisRef: Preferences, property: KProperty<*>) = thisRef[key]

    override operator fun setValue(thisRef: Preferences, property: KProperty<*>, value: T?) {
        require(thisRef is MutablePreferences)
        if (value == null) thisRef.remove(key)
        else thisRef[key] = value
    }
}

internal fun <T : Any> preference(key: Preferences.Key<T>): ReadWriteProperty<Preferences, T?> =
    PreferenceDelegate(key)
