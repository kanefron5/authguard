package dev.zabolotskikh.authentificator.domain.repository

import dev.zabolotskikh.authentificator.domain.model.Service
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun getAllServices(): Flow<List<Service>>

    fun getServiceById(id: Int): Flow<Service>

    suspend fun insert(service: Service)

    suspend fun delete(service: Service)
}