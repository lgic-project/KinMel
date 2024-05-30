plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.kinmel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kinmel"
        minSdk = 26

        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation("com.google.android.material:material:1.7.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.core:core:1.7.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.khalti:checkout-android:0.05.00")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation ("androidx.cardview:cardview:1.0.0")
}