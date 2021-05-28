package com.inventiv.gastropaysdk.utils

import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

internal object AESHelper {
    const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidParameterSpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun encrypt(message: String, privateKey: String): ByteArray {
        val secret: SecretKey = SecretKeySpec(privateKey.toByteArray(), "AES")
        /* Encrypt the message. */
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        return cipher.doFinal(message.toByteArray(StandardCharsets.UTF_8))
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidParameterSpecException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun decrypt(cipherText: ByteArray?, privateKey: String): String {
        val secret: SecretKey = SecretKeySpec(privateKey.toByteArray(), "AES")
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher.doFinal(cipherText), StandardCharsets.UTF_8)
    }
}