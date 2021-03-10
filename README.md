# Swirepay Android SDK


Swirepay Android SDK helps developers implement a native payment experience in their Android application. The SDK requires minimal setup to get started and helps developers process payments under 30 seconds while being PCI compliant. 

The SDK supports payment flows including simple checkout, saving payment methods, paying invoice, setting up subscriptions and more.


Table of contents
=================

<!--ts-->
   * [Installation](#installation)
      * [Requirements](#requirements)
      * [Configuration](#configuration)
      * [Releases](#releases)
      * [Proguard](#proguard)
   * [Features](#features)
   * [Getting Started](#getting-started)
   * [Examples](#examples)
<!--te-->

## Installation
Our SDK is now out of open beta and available in maven central in 1-2 business days. To get the latest SDK please use the link below:
https://drive.google.com/drive/folders/1KohA6xuzMhWkmsL0u4VN7WlBQB85hTW-?usp=sharing

### Requirements

* Android 5.0 (API level 21) and above
* [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin) 3.5.1
* [Gradle](https://gradle.org/releases/) 5.4.1+
* [AndroidX](https://developer.android.com/jetpack/androidx/) (as of v11.0.0)

### Configuration

Add `swirepay-android` to your `build.gradle` dependencies.

```
dependencies {
    implementation 'com.swirepay:swirepay_android:1.0.0'
}
```

### Releases
* The [changelog](CHANGELOG.md) provides a summary of changes in each release.

### Proguard

The Swirepay Android SDK will configure your app's Proguard rules using [proguard-rules.txt](android_sdk/proguard-rules.pro).

## Getting Started
The `SwirepaySdk` class is the entry-point to the Swirepay SDK. It must be instantiated with a [Swirepay publishable/Secret key].

When testing, you can use a test publishable key. Remember to replace the test key with your live key in production. You can view your API keys in the [Swirepay Dashboard](https://dashboard.swirepay.com/developer).

```Java
SwirepaySdk.init("pk_test_key")
```
