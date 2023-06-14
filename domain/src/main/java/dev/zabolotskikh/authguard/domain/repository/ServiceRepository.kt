package dev.zabolotskikh.authguard.domain.repository

import dev.zabolotskikh.authguard.domain.model.Service
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun getAllServices(): Flow<List<Service>>

    fun getServiceById(id: Int): Flow<Service>

    suspend fun insert(service: Service)

    suspend fun delete(service: Service)

    suspend fun update(service: Service)

    suspend fun clear()
}