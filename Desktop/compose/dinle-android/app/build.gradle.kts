plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" // Depends on your kotlin version
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "tm.bent.dinle.hinlen"
    compileSdk = 34

    //key store pass bereketbendi
    //key alias key0
    //key pass bereketbendi

    defaultConfig {
        applicationId = "tm.bent.dinle.hinlen"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }



    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

            ndk {
                debugSymbolLevel
                "SYMBOL_TABLE"
            }
        }
        debug {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            ndk {
                debugSymbolLevel
                "SYMBOL_TABLE"

                abiFilters
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0-alpha02")
    implementation("androidx.activity:activity-ktx:1.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


//    implementation ("com.google.accompanist:accompanist-pager:0.36.0")
//    implementation ("com.google.accompanist:accompanist-pager-indicators:0.36.0")


    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
//    implementation("io.coil-kt:coil-transformations:2.4.0")

    //Cloudy
//    implementation("com.github.skydoves:cloudy:0.1.2")
//    implementation("com.github.skydoves:cloudy:0.2.1")

//    implementation("dev.chrisbanes.haze:haze:0.6.0")

//    implementation("io.coil-kt:coil:2.4.0") // Замените на последнюю версию
//    implementation("io.coil-kt:coil-transformations:2.4.0")


    implementation("io.github.raamcosta.compose-destinations:core:1.10.2")
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.10.2")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.10.2")


    


    //DI
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //Lifecycle
    val lifecycle = "2.5.0"

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //Db
    implementation("androidx.room:room-runtime:2.6.0-beta01")
    annotationProcessor("androidx.room:room-compiler:2.6.0-beta01")
    kapt("androidx.room:room-compiler:2.6.0-beta01")
    implementation("androidx.room:room-ktx:2.6.0-beta01")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    //Paging
    val paging = "3.3.0-alpha02"
    implementation("androidx.paging:paging-runtime-ktx:$paging")
    implementation("androidx.paging:paging-compose:$paging")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.github.skydoves:landscapist-glide:2.3.0")
//    implementation("com.github.skydoves:landscapist-transformation:2.3.0")


    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.11.4")

    implementation("androidx.datastore:datastore-preferences:1.0.0")


    //ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.3.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.3.0")
    implementation("androidx.media3:media3-datasource-okhttp:1.3.0")
    implementation("androidx.media3:media3-datasource-cronet:1.3.0")
    implementation("androidx.media3:media3-session:1.3.0")
    implementation("androidx.media3:media3-ui:1.3.0")

    implementation("com.github.skydoves:flexible-bottomsheet-material3:0.1.2")

    implementation("com.airbnb.android:lottie-compose:4.0.0")
//    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")


}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

