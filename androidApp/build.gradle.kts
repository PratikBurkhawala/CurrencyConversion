plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.android)
}

android {
    namespace = AndroidSdk.applicationId
    compileSdk = AndroidSdk.compileSdkVersion
    defaultConfig {
        applicationId = AndroidSdk.applicationId
        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeComplier
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(BuildModules.shared))
    implementation(AndroidDependencies.androidCore)
    implementation(AndroidDependencies.composeUI)
    implementation(AndroidDependencies.composeMaterial)
    implementation(AndroidDependencies.composeTooling)
    implementation(AndroidDependencies.composeToolingPreview)
    implementation(AndroidDependencies.composeUtil)
    implementation(AndroidDependencies.composeActivity)
    implementation(AndroidDependencies.composeFoundation)
    implementation(AndroidDependencies.koinAndroid)
    implementation(AndroidDependencies.koinCompose)
}