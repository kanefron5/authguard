package dev.zabolotskikh.authentificator.domain.model

data class Service(
    val name: String,
    val privateKey: String,
    val id: Int = 0,
    val timeoutTime: Long = 0,
    val codeTtl: Long = 0,
    val currentCode: String = ""
)
