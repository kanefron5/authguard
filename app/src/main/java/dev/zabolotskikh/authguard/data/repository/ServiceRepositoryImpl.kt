package dev.zabolotskikh.authguard.data.repository

import dev.zabolotskikh.authguard.data.local.dao.ServiceDao
import dev.zabolotskikh.authguard.data.local.entities.ServiceEntity
import dev.zabolotskikh.authguard.data.local.entities.toService
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceDao: ServiceDao
) : ServiceRepository {
    override fun getAllServices() = serviceDao.getServices().map {
        it.map { entity -> entity.toService() }
    }

    override fun getServiceById(id: Int) = serviceDao.getServiceById(id).map {
        it.toService()
    }

    override suspend fun insert(service: Service) = serviceDao.insert(
        ServiceEntity(
            service.name, service.privateKey, service.generationMethod, service.isFavorite
        )
    )

    override suspend fun delete(service: Service) = serviceDao.deleteService(
        ServiceEntity(
            service.name,
            service.privateKey,
            service.generationMethod,
            service.isFavorite,
            service.id,
        )
    )

    override suspend fun update(service: Service) = serviceDao.update(
        ServiceEntity(
            service.name,
            service.privateKey,
            service.generationMethod,
            service.isFavorite,
            service.id,
        )
    )

    override suspend fun clear() = serviceDao.deleteAll()
}