package org.hinsun.core.storage

interface CryptoStorage {
    fun writeWithEncrypt(key: String, value: String): Boolean
    fun readWithDecrypt(key: String): String
}