plugins {
    kotlin(Plugins.multiplatform)
    id(Plugins.androidLibrary)
    id(Plugins.sqlDelight)
    kotlin(Plugins.kotlinXSerialization) version Versions.kotlin
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(MultiplatformDependencies.koinCore)
                implementation(MultiplatformDependencies.ktorCore)
                implementation(MultiplatformDependencies.ktorClientEngine)
                implementation(MultiplatformDependencies.ktorClientLogging)
                implementation(MultiplatformDependencies.ktorContentNegotiation)
                implementation(MultiplatformDependencies.ktorSerializationJson)
                implementation(MultiplatformDependencies.sqlDelight)
                implementation(MultiplatformDependencies.sqlDelightCoroutine)
                implementation(MultiplatformDependencies.kotlinxCoroutines)
                implementation(MultiplatformDependencies.logger)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin(MultiplatformDependencies.kotlinTest))
                implementation(kotlin(MultiplatformDependencies.kotlinTestAnnotation))
                implementation(MultiplatformDependencies.ktorMock)
                implementation(MultiplatformDependencies.kotlinxTestResources)
                implementation(MultiplatformDependencies.kotlinxCoroutines)
                implementation(MultiplatformDependencies.kotlinxCoroutinesTest)
                implementation(MultiplatformDependencies.sqlDelightTest)
                implementation(MultiplatformDependencies.logger)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(MultiplatformDependencies.sqlDelightAndroid)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(AndroidDependencies.junitTest)
                implementation(kotlin(AndroidDependencies.jnuit))
                implementation(AndroidDependencies.androidTestCore)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(MultiplatformDependencies.sqlDelightIos)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = AndroidSdk.applicationId
    compileSdk = AndroidSdk.compileSdkVersion
    defaultConfig {
        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

sqldelight {
    database("RatesDatabase") {
        packageName = "com.pratik.currencyconversion.data.db"
    }
}