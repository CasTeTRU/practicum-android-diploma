plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "API_ACCESS_TOKEN",
            value = "\"${developProperties.apiAccessToken}\""
        )

        buildConfigField(
            type = "String",
            name = "BASE_URL",
            value = "\"https://practicum-diploma-8bc38133faba.herokuapp.com/\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // UI layer
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)
    implementation(libs.ui.viewpager2)

    // Lifecycle
    implementation(libs.lifecycle.viewmodelKtx)
    implementation(libs.lifecycle.runtimeKtx)
    implementation(libs.lifecycle.livedataKtx)
    implementation(libs.lifecycle.viewmodelSavedstate)

    // UI Components
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    // Navigation
    implementation(libs.navigation.fragmentKtx)
    implementation(libs.navigation.uiKtx)

    // Network
    implementation(libs.network.retrofit)
    implementation(libs.network.converterGson)
    implementation(libs.network.okhttp)
    implementation(libs.network.loggingInterceptor)

    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Image Loading
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Dependency Injection
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // region Unit tests
    testImplementation(libs.unitTests.junit)

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
}
