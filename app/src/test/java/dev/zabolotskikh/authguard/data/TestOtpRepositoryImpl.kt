package dev.zabolotskikh.authguard.data

import dev.zabolotskikh.authguard.OtpInstance
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

class TestOtpRepositoryImpl(
    private val serviceRepository: ServiceRepository
) : OtpRepository {
    override fun getAllServices() = serviceRepository.getAllServices().flatMapLatest { data ->
        flow {
            while (currentCoroutineContext().isActive) {
                emit(OtpInstance(data).calculate())
                delay(1000)
            }
        }
    }
}