package dev.zabolotskikh.authguard

import dev.zabolotskikh.authguard.domain.model.Service
import org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val CODE_TTL_SECONDS = 30L

class OtpInstance(
    private val services: List<Service>
) {

    private fun getCode(secret: String): String {
        val epochSeconds = System.currentTimeMillis() / 1000

        val t = epochSeconds / CODE_TTL_SECONDS

        val hs = hmacSha1(secret.base32ToByteArray(), t.toByteArray()).toHexString()
        if (hs.isBlank()) return ""
        val offset = hs.last().digitToInt(16)
        val dbc1 = hs.substring(offset * 2, offset * 2 + 8)

        val dbc2 = dbc1.replaceFirstChar {
            if (it > '7') (it.digitToInt(16) and 0x7).toString(16).first()
            else it
        }

        return (dbc2.toLong(16) % 1_000_000).toString().padStart(6, '0')
    }

    private fun getTtl(): Long {
        return remainingTime(System.currentTimeMillis() / 1000)
    }

    fun calculate() = services.map { it.copy(
        timeoutTime = CODE_TTL_SECONDS,
        currentCode = getCode(it.privateKey),
        codeTtl = getTtl()
    ) }

    private fun hmacSha1(key: ByteArray, message: ByteArray): ByteArray {
        if (key.isEmpty()) return byteArrayOf()
        val hmacSha1 = Mac.getInstance("HmacSHA1")
        val secretKeySpec = SecretKeySpec(key, "HmacSHA1")
        hmacSha1.init(secretKeySpec)
        return hmacSha1.doFinal(message)
    }

    private fun remainingTime(time: Long): Long = CODE_TTL_SECONDS - (time % CODE_TTL_SECONDS)

    private fun Long.toByteArray(): ByteArray =
        ByteBuffer.allocate(Long.SIZE_BYTES).putLong(this).array()

    private fun ByteArray.toHexString() = joinToString("") { byte -> "%02x".format(byte) }

    private fun String.base32ToByteArray() = Base32().decode(this)
}