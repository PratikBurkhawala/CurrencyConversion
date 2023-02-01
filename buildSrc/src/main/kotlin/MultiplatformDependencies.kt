object MultiplatformDependencies {

    const val rootProjectName = "CurrencyConversion"

    const val kotlinxCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"

    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"

    const val kotlinxSerialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"

    const val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorClientEngine = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktorClientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    const val ktorSerializationJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ktoriOS = "io.ktor:ktor-client-ios:${Versions.ktor}"
    const val ktorMock = "io.ktor:ktor-client-mock:${Versions.ktor}"

    const val sqlDelight = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
    const val sqlDelightCoroutine =
        "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val sqlDelightIos = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    const val sqlDelightTest = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"

    const val kotlinTest = "test"
    const val kotlinTestAnnotation = "test-annotations-common"

    const val kotlinxTestResources = "com.goncalossilva:resources:${Versions.kotlinxTestResources}"
    const val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"

    const val logger = "io.github.aakira:napier:${Versions.napierVersion}"
}