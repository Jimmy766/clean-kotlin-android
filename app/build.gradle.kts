plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.kapt)
  alias (libs.plugins.android.hilt)
}

android {
  namespace = "jn.countries.clean.app"
  compileSdk = 36

  defaultConfig {
    applicationId = "jn.countries.clean.app"
    minSdk = 29
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
  implementation(libs.retrofit2)
  implementation(libs.retrofit2.gson)
  implementation(libs.okhttp3)
  implementation(libs.okhttp3.logging.interceptor)
  implementation(libs.androidx.viewmodel.compose)
  implementation(libs.androidx.runtime.compose)
  implementation(libs.kotlin.serialization.json)
  implementation(libs.retrofit2.serialization)
  implementation(libs.coil)
  implementation(libs.coil.svg)
  implementation(libs.navigation.compose)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.room)
  implementation(libs.room.ktx)
  kapt(libs.room.compiler)
}