package com.rudradave.kmpfintechstarter.shared.data.local

import com.russhwolf.settings.Settings

/** Provides access to securely stored credentials and keys. */
interface SecureStorage {
    /** Returns the passphrase for database encryption. Generates one if it doesn't exist. */
    fun getDatabasePassphrase(): String
}

internal class SecureStorageImpl(private val settings: Settings) : SecureStorage {
    override fun getDatabasePassphrase(): String {
        val existing = settings.getStringOrNull(KEY_PASSPHRASE)
        if (existing != null) return existing
        
        val newPassphrase = generateRandomPassphrase()
        settings.putString(KEY_PASSPHRASE, newPassphrase)
        return newPassphrase
    }

    private fun generateRandomPassphrase(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+"
        return (1..32).map { chars.random() }.joinToString("")
    }

    companion object {
        private const val KEY_PASSPHRASE = "db_passphrase"
    }
}
