import java.io.FileInputStream
import java.util.*

object Config {
    const val COMPILE_SDK = 33
    const val MIN_SDK = 21
    const val TARGET_SDK = 33

    const val GRADLE_PLUGIN = "com.android.tools.build:gradle:7.3.1"

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
}