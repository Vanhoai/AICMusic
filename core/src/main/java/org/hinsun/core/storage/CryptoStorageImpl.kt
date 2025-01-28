package org.hinsun.core.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import org.hinsun.core.BuildConfig
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import android.util.Base64

class CryptoStorageImpl @Inject constructor(
    private val hinsunStorage: HinsunStorage
) {
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    private var cipher: Cipher = Cipher.getInstance(TRANSFORMATION)

    init {
        keyStore.load(null)
        // If key alias doesn't exist, create a new secret key
        if (!keyStore.containsAlias(KEY_ALIAS)) createSecretKey()
    }

    private val publicKey: PublicKey
        get() = keyStore.getCertificate(KEY_ALIAS).publicKey

    private val privateKeyEntry: KeyStore.PrivateKeyEntry
        get() = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry

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

    private fun encrypt(value: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypt = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    private fun decrypt(value: String): String {
        val privateKey = privateKeyEntry.privateKey

        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decrypt = cipher.doFinal(Base64.decode(value, Base64.DEFAULT))
        return String(decrypt)
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