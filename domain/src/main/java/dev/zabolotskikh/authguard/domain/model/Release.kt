package dev.zabolotskikh.authguard.domain.model

data class Release(val version: String, val changelog: Set<ChangelogItem>)
