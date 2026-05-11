package com.rudradave.kmpfintechstarter.shared.util.security

import platform.Foundation.NSFileManager

/** Returns true if the iOS device is suspected to be jailbroken. */
actual fun isDeviceRooted(): Boolean {
    val fileManager = NSFileManager.defaultManager
    val paths = arrayOf(
        "/Applications/Cydia.app",
        "/Library/MobileSubstrate/MobileSubstrate.dylib",
        "/bin/bash",
        "/usr/sbin/sshd",
        "/etc/apt",
        "/private/var/lib/apt/"
    )
    for (path in paths) {
        if (fileManager.fileExistsAtPath(path)) return true
    }
    return false
}
