package dev.zabolotskikh.authguard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.zabolotskikh.authguard.data.local.entities.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM service_table ORDER BY is_favorite DESC")
    fun getServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM service_table where id=:serviceId")
    fun getServiceById(serviceId: Int): Flow<ServiceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(service: ServiceEntity)

    @Query("DELETE FROM service_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)
}