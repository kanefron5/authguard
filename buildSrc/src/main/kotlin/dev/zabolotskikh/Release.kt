package dev.zabolotskikh

data class Release(val version: String, val changelog: Set<ChangelogItem>)