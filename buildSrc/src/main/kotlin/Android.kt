object AndroidSdk {
    const val appName = "currencyconversion"
    const val applicationId = "com.pratik.currencyconversion.android"

    const val minSdkVersion = 21
    const val compileSdkVersion = 33
    const val targetSdkVersion = compileSdkVersion

    const val versionCode = 1
    const val versionName = "1.0"

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object AndroidDependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

    const val androidCore = "androidx.core:core-ktx:1.9.0"

    const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:1.3.1"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val composeUtil = "androidx.compose.ui:ui-util:${Versions.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:1.3.1"
    const val composeConstraintLayout =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.compose_constraint}"
    const val composeActivity =
        "androidx.activity:activity-compose:${Versions.compose_activity}"
    const val material3 = "androidx.compose.material3:material3:${Versions.composeMat3}"

    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    // Koin-Dependency injection
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"

    const val androidTestCore = "androidx.test:core:${Versions.androidTestCore}"

    const val jnuit = "test-junit"
    const val junitTest = "junit:junit:4.13.2"
}