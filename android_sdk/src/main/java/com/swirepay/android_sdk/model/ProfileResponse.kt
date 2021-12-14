package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileResponse(
    val user: String,
    val accountGid: String,
    val portalGid: String,
    val test: Boolean
) : Parcelable