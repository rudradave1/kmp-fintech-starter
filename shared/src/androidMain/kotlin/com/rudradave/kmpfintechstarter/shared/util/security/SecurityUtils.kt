package com.rudradave.kmpfintechstarter.shared.util.security

import android.os.Build
import java.io.File

/** Returns true if the Android device is suspected to be rooted. */
actual fun isDeviceRooted(): Boolean {
    val buildTags = Build.TAGS
    if (buildTags != null && buildTags.contains("test-keys")) return true

    val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )
    for (path in paths) {
        if (File(path).exists()) return true
    }
    return false
}
