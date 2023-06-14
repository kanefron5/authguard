package dev.zabolotskikh.passlock.data

import android.util.Base64
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import java.security.MessageDigest
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal class PasscodeEncoderImpl(
    private val password: String
): PasscodeEncoder {
    override fun encode(input: String): ByteArray? {
        try {
            val digestOfPassword =
                MessageDigest.getInstance("MD5").digest(password.toByteArray())

            val keyBytes = Arrays.copyOf(digestOfPassword, 24)

            val padding = 8
            var k = 16
            for (j in 0 until padding) keyBytes[k++] = keyBytes[j]

            val cipherText = Cipher.getInstance("DESede/CBC/PKCS5Padding").apply {
                init(
                    Cipher.ENCRYPT_MODE,
                    SecretKeySpec(keyBytes, "DESede"),
                    IvParameterSpec(ByteArray(padding))
                )
            }.doFinal(input.toByteArray())

            return Base64.encode(cipherText, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}