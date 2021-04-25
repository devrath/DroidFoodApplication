// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
   /* ext.kotlin_version = "1.4.31"



    ext.jsoup_version = "1.13.1"
    ext.shimmer_recyclerview_version = "0.4.0"
    ext.facebook_shimmer_version = "0.5.0"
    ext.gson_version = "2.8.6"
    ext.coil_version = "0.13.0"
    ext.lifecycle_runtime_viewmodel_livedata_version = "2.3.1"
    ext.lifecycle_extension_version = "2.2.0"
    ext.coroutines_version = "1.4.2"
    ext.dagger_hilt_version = "2.35"
    ext.retrofit_version = "2.9.0"
    ext.recycler_view_version = "1.2.0"
    ext.data_stores_version = "1.0.0-alpha01"
    ext.room_version = "2.2.6"
    ext.material_components_version = "1.4.0-alpha02"
    ext.constraint_layout_version = "2.0.4"
    ext.legacy_support_version = "1.0.0"
    ext.navigation_version = "2.3.5"
    ext.junit_version = "4.13.2"
    ext.android_x_junit_version = "1.1.2"
    ext.espresso_core_version = "3.3.0"
    ext.app_compact_version = "1.2.0"
    ext.core_ktx_version = "1.3.2"
    ext.stdlib_version = "1.4.32"

*/

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