plugins {
    `kotlin-dsl`
}

group = "com.rudradave.buildlogic"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.composeMultiplatform.get()}")
    implementation("app.cash.sqldelight:gradle-plugin:${libs.versions.sqldelight.get()}")
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "com.rudradave.kmp.library"
            implementationClass = "KmpLibraryPlugin"
        }
        register("androidApp") {
            id = "com.rudradave.android.app"
            implementationClass = "AndroidAppPlugin"
        }
    }
}
