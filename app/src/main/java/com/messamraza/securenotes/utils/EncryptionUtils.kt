package com.messamraza.securenotes.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val SECRET_KEY = "MySecretKey12345" // In production, use Android Keystore

    private fun getSecretKey(): SecretKey {
        return SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
    }

    fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val encryptedData = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encryptedData, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            data // Return original data if encryption fails
        }
    }

    fun decrypt(encryptedData: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey())
            val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
            val decryptedData = cipher.doFinal(decodedData)
            String(decryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
            encryptedData // Return encrypted data if decryption fails
        }
    }
}
