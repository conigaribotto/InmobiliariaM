
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.12.3" apply false
        id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "Inmobiliaria"
include(":app")
