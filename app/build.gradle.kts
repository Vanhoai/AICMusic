import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
}

val keyStoreFile = rootProject.file("keystore.properties")
val keyStoreProperties = Properties()
if (keyStoreFile.exists()) keyStoreProperties.load(keyStoreFile.inputStream())

val localFile = rootProject.file("local.properties")
val localProperties = Properties()
if (localFile.exists()) localProperties.load(localFile.inputStream())

android {
    namespace = "org.hinsun.music"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.hinsun.music"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(keyStoreProperties["storeFile"].toString())
            storePassword = keyStoreProperties["storePassword"].toString()
            keyAlias = keyStoreProperties["keyAlias"].toString()
            keyPassword = keyStoreProperties["keyPassword"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "WEB_CLIENT_ID",
                "\"${localProperties.getProperty("webClientId")}\""
            )

            buildConfigField(
                "String",
                "BIOMETRIC_KEY",
                "\"${localProperties.getProperty("biometricKey")}\""
            )

            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            buildConfigField(
                "String",
                "WEB_CLIENT_ID",
                "\"${localProperties.getProperty("webClientId")}\""
            )

            buildConfigField(
                "String",
                "BIOMETRIC_KEY",
                "\"${localProperties.getProperty("biometricKey")}\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(kotlin("reflect"))

    // implementation(project(":processor"))
    // ksp(project(mapOf("path" to ":processor", "configuration" to "releaseRuntimeElements")))

    // Ksp
    implementation(libs.ksp.processing)

    // Timber
    implementation(libs.timber)

    // Media3
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.common)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // room database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // Glide
    implementation(libs.glide)
    implementation(libs.androidx.media)

    // Coil
    implementation(libs.coil.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)

    // Play service auth
    implementation(libs.play.services.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    // Biometric
    implementation(libs.biometric)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
}

ksp {
    arg("verbose", "true")
}