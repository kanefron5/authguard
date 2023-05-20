package dev.zabolotskikh.authguard.data

import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class TestServiceRepositoryImpl: ServiceRepository {
    val list = ArrayList<Service>()
    val flow = MutableSharedFlow<ArrayList<Service>>()
    override fun getAllServices(): Flow<List<Service>> = flow

    override fun getServiceById(id: Int) = flow {
        list.findLast { it.id == id }?.apply { emit(this) }
    }

    override suspend fun insert(service: Service) {
        list.add(service.copy(id = list.size))
        flow.emit(list)
    }

    override suspend fun delete(service: Service) {
        val indexOfFirst = list.indexOfFirst { it.id == service.id }
        if (indexOfFirst >= 0) {
            list.removeAt(indexOfFirst)
            flow.emit(list)
        }
    }

    override suspend fun update(service: Service) {
        val indexOfFirst = list.indexOfFirst { it.id == service.id }
        if (indexOfFirst >= 0) {
            list[indexOfFirst] = service
            flow.emit(list)
        }
    }

    override suspend fun clear() {
        list.clear()
        flow.emit(list)
    }
}