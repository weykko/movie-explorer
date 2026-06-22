plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.urfu.movie_explorer.core.common"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":core:domain"))

    // Compose BOM + базовый набор UI-зависимостей экспортируется как api,
    // потому что все feature-модули используют один и тот же набор Compose API.
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.navigation.compose)
    api(libs.kotlinx.serialization.json)
    api(libs.coil.compose)

    // Koin для @Composable koinInject() в `MovieCard`.
    api(libs.koin.androidx.compose)
    api(libs.koin.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
