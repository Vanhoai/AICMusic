package org.hinsun.core.storage

import javax.crypto.Cipher

interface CryptoStorage {
    fun initEncryptionCipher(): Cipher
    fun initDecryptionCipher(initializationVector: ByteArray): Cipher
    fun encrypt(value: String, cipher: Cipher): EncryptedData
    fun decrypt(value: ByteArray, cipher: Cipher): String
    fun writeWithEncrypt(key: String, value: EncryptedData): Boolean
    fun readWithDecrypt(key: String): EncryptedData?
}