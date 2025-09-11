plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hemofarm.g_track"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hemofarm.g_track"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE"
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}
dependencies {
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") // QR kod skener
    implementation("com.google.zxing:core:3.4.1")
}
dependencies {
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version") // za Javu
    // Testiranje
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

dependencies {
    implementation(libs.jakarta.mail)
    implementation(libs.jakarta.activation)
}


