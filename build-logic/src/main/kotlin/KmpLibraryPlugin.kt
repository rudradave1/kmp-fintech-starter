import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

class KmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        pluginManager.apply("app.cash.sqldelight")

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
            iosX64()
            iosArm64()
            iosSimulatorArm64()

            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }

        extensions.configure<LibraryExtension> {
            compileSdk = 35
            namespace = "com.rudradave.kmpfintechstarter.shared"

            defaultConfig {
                minSdk = 26
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
                isCoreLibraryDesugaringEnabled = true
            }
        }
    }
}
