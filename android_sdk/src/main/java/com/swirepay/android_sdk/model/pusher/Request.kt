package com.swirepay.android_sdk.model.pusher

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Request(
    val message: String?
) : Parcelable