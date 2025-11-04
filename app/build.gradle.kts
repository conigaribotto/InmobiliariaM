plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.inmobiliaria"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.inmobiliaria"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // UI base
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ======= IMPORTANTES: versiones Java (SIN -ktx) =======
    // Lifecycle (Java)
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")

    // Navigation (Java)
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")

    // Compatibilidad antigua (si realmente la necesitás)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Retrofit + OkHttp + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Desugar (para APIs de java.time, etc.)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Defensa extra por si alguna lib vieja mete jdk7/jdk8 (1.6.21)
configurations.all {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
}
