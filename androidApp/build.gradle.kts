plugins {
    id("com.rudradave.android.app")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.compose.navigation)
    implementation(libs.koin.compose)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.splashscreen)
    implementation(libs.bundles.compose.ui)
    implementation(libs.kotlinx.serialization.json)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    debugImplementation(libs.compose.ui.tooling)
}
