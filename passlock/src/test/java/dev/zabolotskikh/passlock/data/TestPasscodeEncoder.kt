package dev.zabolotskikh.passlock.data

import dev.zabolotskikh.passlock.domain.PasscodeEncoder

class TestPasscodeEncoder: PasscodeEncoder {
    override fun encode(input: String): ByteArray = input.toByteArray()
}