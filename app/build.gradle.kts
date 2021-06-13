import com.demo.code.dependencies.Apps
import com.demo.code.dependencies.Libs
import com.demo.code.dependencies.Kpt
import com.demo.code.dependencies.KaptTest
import com.demo.code.dependencies.KaptAndroidTest
import com.demo.code.dependencies.AndroidTestImplementation
import com.demo.code.dependencies.TestImplementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        applicationId(Apps.applicationId)
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode(Apps.versionCode)
        versionName(Apps.versionName)
        multiDexEnabled=true
        testInstrumentationRunner(Apps.testRunner)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled=false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { kotlinOptions.jvmTarget = "1.8" }
    buildFeatures { viewBinding=true }
}

kapt { correctErrorTypes=true }

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Libs.stdlib)
    implementation(Libs.core_ktx)
    implementation(Libs.app_compact)
    implementation(Libs.constraint_layout)
    implementation(Libs.legacy_support)
    implementation(Libs.navigation_fragment)
    implementation(Libs.navigation_ui)
    implementation(Libs.material_components)
    implementation(Libs.room_runtime)
    implementation(Libs.room_ktx)
    implementation(Libs.data_stores)
    implementation(Libs.recycler_view)
    implementation(Libs.retrofit)
    implementation(Libs.retrofit_gson)
    implementation(Libs.dagger_hilt)
    implementation(Libs.coroutines_core)
    implementation(Libs.coroutines_android)
    implementation(Libs.lifecycle_extension)
    implementation(Libs.lifecycle_runtime)
    implementation(Libs.lifecycle_viewmodel)
    implementation(Libs.lifecycle_livedata)
    implementation(Libs.coil)
    implementation(Libs.gson)
    implementation(Libs.facebook_shimmer)
    implementation(Libs.shimmer_recyclerview)
    implementation(Libs.jsoup)

    androidTestImplementation(AndroidTestImplementation.hilt_android_test)
    androidTestImplementation(AndroidTestImplementation.hilt_room_android_test)
    androidTestImplementation(AndroidTestImplementation.junit_android_x_android_test)
    androidTestImplementation(AndroidTestImplementation.espresso_android_test)

    kapt(Kpt.room_kpt)
    kapt(Kpt.dagger_compiler_kpt)

    kaptTest(KaptTest.hilt_kapt_test)

    kaptAndroidTest(KaptAndroidTest.hilt_kapt_Android_Test)

    testImplementation(TestImplementation.junit)
    testImplementation(TestImplementation.hiltTesting)
}