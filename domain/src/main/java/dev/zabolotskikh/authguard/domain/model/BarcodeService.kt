package dev.zabolotskikh.authguard.domain.model

data class BarcodeService(
    val alias: String, val secret: String, val issuer: String
)