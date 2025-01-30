package org.hinsun.core.storage

interface HinsunStorage {
    fun <T> write(key: String, value: T): Boolean
    fun <T> read(key: String, defaultValue: T): T
    fun remove(key: String): Boolean
    fun clear(): Boolean
}