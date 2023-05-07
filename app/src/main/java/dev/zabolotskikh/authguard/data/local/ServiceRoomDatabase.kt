package dev.zabolotskikh.authguard.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.zabolotskikh.authguard.data.local.dao.AppStateDao
import dev.zabolotskikh.authguard.data.local.entities.ServiceEntity
import dev.zabolotskikh.authguard.data.local.dao.ServiceDao
import dev.zabolotskikh.authguard.data.local.entities.AppStateEntity

@Database(
    entities = [ServiceEntity::class, AppStateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ServiceRoomDatabase() : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun appStateDao(): AppStateDao

    companion object {
        @Volatile
        private var INSTANCE: ServiceRoomDatabase? = null

        fun getDatabase(context: Context): ServiceRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ServiceRoomDatabase::class.java,
                    "service_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}