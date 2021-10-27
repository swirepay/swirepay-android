package com.swirepay.android_sdk.model.pusher

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppConfig(
    val appId: String,
    val appKey: String,
    val secret: String,
    val posKey: String,
) : Parcelable