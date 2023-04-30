package dev.zabolotskikh.authentificator.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.zabolotskikh.authentificator.data.local.entities.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM service_table ORDER BY id ASC")
    fun getServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM service_table where id=:serviceId")
    fun getServiceById(serviceId: Int): Flow<ServiceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(service: ServiceEntity)

    @Query("DELETE FROM service_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteService(service: ServiceEntity)
}