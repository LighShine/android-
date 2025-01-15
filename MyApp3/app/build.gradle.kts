plugins {
    //alias(libs.plugins.android.application)
    //alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    //需要使用kotlin注解插件
    id ("kotlin-kapt")
    //启用Dagger组件
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.lizhiheng.myapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lizhiheng.myapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("com.google.accompanist:accompanist-pager:0.24.13-rc")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.24.13-rc")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.13-rc")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.13-rc")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.9-beta")


    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.google.accompanist:accompanist-permissions:0.23.1")
    implementation ("androidx.core:core-ktx:1.12.0")

    implementation ("androidx.compose.ui:ui:1.0.0")

    implementation ("androidx.compose.ui:ui-tooling-preview:1.0.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    //协程
    val coroutines_version = "1.7.1"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    //Room
    val room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    testImplementation ("androidx.room:room-testing:$room_version")

    //Hilt
    implementation ("com.google.dagger:hilt-android:2.45")
    kapt ("com.google.dagger:hilt-android-compiler:2.45")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    // livedata
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.4")
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("androidx.compose.material:material:1.0.1")
//引入图标库
    implementation ("androidx.compose.material:material-icons-core:1.6.4")
    implementation ("androidx.compose.material:material-icons-extended:1.6.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}