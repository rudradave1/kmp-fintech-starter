plugins {
    alias(libs.plugins.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.bundles.shared.common.test)
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

android {
    namespace = "com.rudradave.kmpfintechstarter.shared"
}

sqldelight {
    databases {
        create("FintechDatabase") {
            packageName.set("com.rudradave.kmpfintechstarter.shared.data.local.db")
        }
    }
}
