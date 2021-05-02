// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {


    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${com.demo.code.dependencies.Versions.toolsBuildGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.demo.code.dependencies.Versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${com.demo.code.dependencies.Versions.navigation}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${com.demo.code.dependencies.Versions.gradlePlugin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}