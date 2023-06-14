package dev.zabolotskikh.authguard.domain.repository

import dev.zabolotskikh.authguard.domain.model.Release

interface ChangelogRepository {
    fun get(): List<Release>
}