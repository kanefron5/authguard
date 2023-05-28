package dev.zabolotskikh.passlock.domain

interface PasscodeEncoder {
    fun encode(input: String): ByteArray?
}