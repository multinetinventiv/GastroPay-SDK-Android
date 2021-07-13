package com.inventiv.gastropaysdk.helper

import com.inventiv.gastropaysdk.utils.AESHelper
import org.junit.Assert
import org.junit.Test

class AESHelperTest {
    @Test
    fun testAESEncryption() {
        val privateKey = "H@McQfTjWnZr4u7x"
        val plainText = "HelloWorld"
        val encrypted = AESHelper.encrypt(plainText, privateKey)
        println("Encrypted data : ${encrypted.contentToString()}")
        val decrypted = AESHelper.decrypt(encrypted, privateKey)
        println("Decrypted data : $decrypted")
        Assert.assertEquals(plainText, decrypted)
    }
}