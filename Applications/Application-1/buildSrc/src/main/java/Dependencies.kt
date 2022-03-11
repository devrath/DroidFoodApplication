package com.demo.code.dependencies

import com.demo.code.dependencies.Versions.android_x_junit_version
import com.demo.code.dependencies.Versions.app_compact_version
import com.demo.code.dependencies.Versions.coil_version
import com.demo.code.dependencies.Versions.constraint_layout_version
import com.demo.code.dependencies.Versions.core_ktx_version
import com.demo.code.dependencies.Versions.coroutines_version
import com.demo.code.dependencies.Versions.dagger_hilt_version
import com.demo.code.dependencies.Versions.data_stores_version
import com.demo.code.dependencies.Versions.espresso_core_version
import com.demo.code.dependencies.Versions.facebook_shimmer_version
import com.demo.code.dependencies.Versions.gson_version
import com.demo.code.dependencies.Versions.jsoup_version
import com.demo.code.dependencies.Versions.junit_version
import com.demo.code.dependencies.Versions.legacy_support_version
import com.demo.code.dependencies.Versions.lifecycle_extension_version
import com.demo.code.dependencies.Versions.lifecycle_runtime_viewmodel_livedata_version
import com.demo.code.dependencies.Versions.material_components_version
import com.demo.code.dependencies.Versions.navigation_version
import com.demo.code.dependencies.Versions.recycler_view_version
import com.demo.code.dependencies.Versions.retrofit_version
import com.demo.code.dependencies.Versions.room_version
import com.demo.code.dependencies.Versions.shimmer_recyclerview_version
import com.demo.code.dependencies.Versions.stdlib_version

object Apps {
    const val compileSdk = 30
    const val minSdk = 16
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"
    const val buildToolsVersion = "30.0.3"
    const val applicationId = "com.demo.code"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Versions {
    const val toolsBuildGradle = "4.1.3"
    const val kotlin = "1.4.31"
    const val navigation = "2.3.1"
    const val gradlePlugin = "2.35"

    const val jsoup_version = "1.13.1"
    const val shimmer_recyclerview_version = "0.4.0"
    const val facebook_shimmer_version = "0.5.0"
    const val gson_version = "2.8.6"
    const val coil_version = "0.13.0"
    const val lifecycle_runtime_viewmodel_livedata_version = "2.3.1"
    const val lifecycle_extension_version = "2.2.0"
    const val coroutines_version = "1.4.2"
    const val dagger_hilt_version = "2.35"
    const val retrofit_version = "2.9.0"
    const val recycler_view_version = "1.2.0"
    const val data_stores_version = "1.0.0-alpha01"
    const val room_version = "2.2.6"
    const val material_components_version = "1.4.0-alpha02"
    const val constraint_layout_version = "2.0.4"
    const val legacy_support_version = "1.0.0"
    const val navigation_version = "2.3.5"
    const val junit_version = "4.13.2"
    const val android_x_junit_version = "1.1.2"
    const val espresso_core_version = "3.3.0"
    const val app_compact_version = "1.2.0"
    const val core_ktx_version = "1.3.2"
    const val stdlib_version = "1.4.32"
}

object Libs {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${stdlib_version}"
    const val core_ktx = "androidx.core:core-ktx:${core_ktx_version}"
    const val app_compact = "androidx.appcompat:appcompat:${app_compact_version}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${constraint_layout_version}"
    const val legacy_support = "androidx.legacy:legacy-support-v4:${legacy_support_version}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${navigation_version}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${navigation_version}"
    const val material_components = "com.google.android.material:material:${material_components_version}"
    const val room_runtime = "androidx.room:room-runtime:${room_version}"
    const val room_ktx = "androidx.room:room-ktx:${room_version}"
    const val data_stores = "androidx.datastore:datastore-preferences:${data_stores_version}"
    const val recycler_view = "androidx.recyclerview:recyclerview:${recycler_view_version}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${retrofit_version}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${retrofit_version}"
    const val dagger_hilt = "com.google.dagger:hilt-android:${dagger_hilt_version}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutines_version}"
    const val lifecycle_extension = "androidx.lifecycle:lifecycle-extensions:${lifecycle_extension_version}"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${lifecycle_runtime_viewmodel_livedata_version}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_runtime_viewmodel_livedata_version}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${lifecycle_runtime_viewmodel_livedata_version}"
    const val coil = "io.coil-kt:coil:${coil_version}"
    const val gson = "com.google.code.gson:gson:${gson_version}"
    const val facebook_shimmer = "com.facebook.shimmer:shimmer:${facebook_shimmer_version}"
    const val shimmer_recyclerview = "com.todkars:shimmer-recyclerview:${shimmer_recyclerview_version}"
    const val jsoup = "org.jsoup:jsoup:${jsoup_version}"
}

object KaptAndroidTest {
    const val hilt_kapt_Android_Test =  "com.google.dagger:hilt-compiler:${dagger_hilt_version}"
}

object KaptTest {
    const val hilt_kapt_test = "com.google.dagger:hilt-compiler:${dagger_hilt_version}"
}

object Kpt {
    const val room_kpt =  "androidx.room:room-compiler:${room_version}"
    const val dagger_compiler_kpt =  "com.google.dagger:hilt-compiler:${dagger_hilt_version}"
}

object TestImplementation {
    const val junit = "junit:junit:${junit_version}"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${dagger_hilt_version}"
}

object AndroidTestImplementation {
    const val hilt_android_test =   "com.google.dagger:hilt-android-testing:${dagger_hilt_version}"
    const val hilt_room_android_test =  "androidx.room:room-testing:${room_version}"
    const val junit_android_x_android_test =  "androidx.test.ext:junit:${android_x_junit_version}"
    const val espresso_android_test =  "androidx.test.espresso:espresso-core:${espresso_core_version}"
}