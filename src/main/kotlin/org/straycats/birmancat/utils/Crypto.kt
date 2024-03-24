package org.straycats.birmancat.utils

import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypto(
    key: String,
) {

    private val iv = IvParameterSpec("s42vYaZqtnkmTov8".toByteArray())
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    private val jackson = Jackson.getMapper()

    fun enc(value: Any): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        val json = jackson.writeValueAsString(value)
        val byteArrayOfEncrypt = cipher.doFinal(json.toByteArray(Charsets.UTF_8))

        return Base64.getUrlEncoder().withoutPadding().encodeToString(byteArrayOfEncrypt)
    }

    fun <T> dec(text: String, valueType: Class<T>): T {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        val cipherText = Base64.getUrlDecoder().decode(text)
        val decryptedText = String(cipher.doFinal(cipherText), Charsets.UTF_8)

        return jackson.readValue(decryptedText, valueType)
    }

    companion object {
        fun sha256(value: String): String {
            val bytes = value.toByteArray()
            val md = MessageDigest.getInstance("SHA-512")
            val digest = md.digest(bytes)
            return digest.fold("") { str, it -> str + "%02x".format(it) }
        }
    }
}
