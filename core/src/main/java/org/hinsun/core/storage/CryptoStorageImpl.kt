package org.hinsun.core.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import org.hinsun.core.BuildConfig
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject
import android.util.Base64
import com.google.gson.Gson
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoStorageImpl @Inject constructor(
    private val hinsunStorage: HinsunStorage
) : CryptoStorage {
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE)

    init {
        keyStore.load(null)
        // If key alias doesn't exist, create a new secret key
        if (!keyStore.containsAlias(KEY_ALIAS)) createSecretKey()
    }

    private val publicKey: PublicKey
        get() = keyStore.getCertificate(KEY_ALIAS).publicKey

    private val privateKeyEntry: KeyStore.PrivateKeyEntry
        get() = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry

    private val secretKey: SecretKey
        get() = keyStore.getKey(KEY_ALIAS, null) as SecretKey

    private fun createSecretKey() {
        val keyGenParams = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(BLOCK_MODE)
            setEncryptionPaddings(PADDING)
            setUserAuthenticationRequired(true)
        }.build()

        val keyGenerator = KeyGenerator.getInstance(ALGORITHMS, ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        keyGenerator.generateKey()
    }

    override fun initEncryptionCipher(): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun initDecryptionCipher(initializationVector: ByteArray): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, initializationVector)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher
    }

    override fun encrypt(value: String, cipher: Cipher): EncryptedData {
        val encryptedBytes = cipher.doFinal(value.toByteArray(Charset.forName("UTF-8")))
        return EncryptedData(encryptedBytes, cipher.iv)
    }

    override fun decrypt(value: ByteArray, cipher: Cipher): String {
        val decryptedBytes = cipher.doFinal(value)
        return String(decryptedBytes, Charset.forName("UTF-8"))
    }

    override fun writeWithEncrypt(key: String, value: EncryptedData): Boolean {
        val json = Gson().toJson(value)
        return hinsunStorage.write(key, json)
    }

    override fun readWithDecrypt(key: String): EncryptedData? {
        val json = hinsunStorage.read(key, defaultValue = "")
        if (json.isEmpty()) return null
        return Gson().fromJson(json, EncryptedData::class.java)
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = BuildConfig.KEY_ALIAS

        private const val ALGORITHMS = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE

        private const val TRANSFORMATION = "$ALGORITHMS/$BLOCK_MODE/$PADDING"
    }
}

data class EncryptedData(val ciphertext: ByteArray, val initializationVector: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedData

        if (!ciphertext.contentEquals(other.ciphertext)) return false
        return initializationVector.contentEquals(other.initializationVector)
    }

    override fun hashCode(): Int {
        var result = ciphertext.contentHashCode()
        result = 31 * result + initializationVector.contentHashCode()
        return result
    }
}