plugins{
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.aayush.filmipedia"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName =  "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    externalNativeBuild {
        ndkBuild {
            setPath("src/main/jni/Android.mk")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.31")
    implementation("androidx.appcompat:appcompat:1.1.0-alpha05")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.paging:paging-runtime:2.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0-alpha-05")

    implementation("com.aurelhubert:ahbottomnavigation:2.3.4")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")

    implementation("com.google.android.material:material:1.1.0-alpha06")

    implementation("com.google.code.gson:gson:2.8.5")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.0.0-alpha01")
    implementation("com.squareup.okio:okio:2.2.2")

    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")

    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.0-M1")

    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0-beta01")
}
