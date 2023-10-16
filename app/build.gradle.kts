import com.android.build.gradle.internal.api.BaseVariantOutputImpl

@Suppress("PropertyName")
val VERSION_NAME="2.3.3"
@Suppress("PropertyName")
val VERSION_CODE=17

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
        versionCode = VERSION_CODE
        versionName  = VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DEFAULT_API_KEY",
            (project.properties.get("defaultApiKey") as? String)?.let { "\"$it\"" } ?: "null")
        buildConfigField("String", "DEFAULT_ENDPOINT_URL",
            (project.properties.get("defaultEndpointUrl") as? String)?.let { "\"$it\"" } ?: "null")
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
    val useFpProDebugVersion = false // switch to true when needed to debug the library
    implementation("com.fingerprint.android:pro:$VERSION_NAME${if (useFpProDebugVersion) "-debug" else ""}")

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
