package com.rudradave.kmpfintechstarter.shared.data.local.secure

/** Interface for hardware-backed secure storage of sensitive data. */
interface SecureStorage {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
    fun remove(key: String)
    fun clear()
}
