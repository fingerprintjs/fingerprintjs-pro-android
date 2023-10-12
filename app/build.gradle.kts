import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fingerprintjs.android.fpjs_pro_demo"
        minSdk = 21
        targetSdk = 33
        versionCode = Integer.parseInt(project.property("VERSION_CODE") as String)
        versionName  = project.property("VERSION_NAME") as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias  = System.getenv("RELEASE_SIGN_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_SIGN_KEY_PASSWORD")
        }
        create("releaseLocalSign") {
            storeFile = file("release_local.jks")
            storePassword = "password"
            keyAlias = "key0"
            keyPassword = "password"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        create("releaseLocalSign") {
            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile ("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("releaseLocalSign")
            matchingFallbacks += listOf("release")
        }
    }

    namespace = "com.fingerprintjs.android.fpjs_pro_demo"

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    applicationVariants.all {
        val variant = this
        this.outputs.all {
            (this as? BaseVariantOutputImpl)?.outputFileName = "FPJS-Pro-Playground-${variant.name}-${variant.versionName}.apk"
        }
    }
}

dependencies {
    implementation("com.fingerprint.android:pro:${project.findProperty("VERSION_NAME")}")

    implementation("org.osmdroid:osmdroid-android:6.1.11")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    // see https://github.com/google/tink/issues/702
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06") {
        exclude(group = "com.google.crypto.tink", module = "tink-android")
    }
    implementation("com.google.crypto.tink:tink-android:1.9.0")
}
