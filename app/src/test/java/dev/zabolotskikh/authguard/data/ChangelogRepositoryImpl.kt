package dev.zabolotskikh.authguard.data

import dev.zabolotskikh.authguard.domain.model.Release
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository

class ChangelogRepositoryImpl: ChangelogRepository {
    override fun get(): List<Release> {
        return emptyList()
    }
}