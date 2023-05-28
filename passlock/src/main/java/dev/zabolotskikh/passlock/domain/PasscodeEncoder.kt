package dev.zabolotskikh.passlock.domain

internal interface PasscodeEncoder {
    fun encode(input: String): ByteArray?
}