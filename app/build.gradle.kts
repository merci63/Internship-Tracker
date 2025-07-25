
import java.util.Properties
import java.io.FileInputStream;

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()){
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.my.internshiptracker"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.my.internshiptracker"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        buildConfigField ("String","ADZUNA_APP_ID", "\"${localProperties.getProperty("adzuna.app.id") ?: ""}\"")
        buildConfigField ("String","ADZUNA_API_KEY","\"${localProperties.getProperty("adzuna.app.key") ?: ""}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.android.material:material:<version>")
}