package dev.zabolotskikh.authguard.domain.repository

import dev.zabolotskikh.authguard.domain.model.Service
import kotlinx.coroutines.flow.Flow

interface OtpRepository {
    fun getAllServices(): Flow<List<Service>>
}