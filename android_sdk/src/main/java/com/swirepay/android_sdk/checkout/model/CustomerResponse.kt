package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val phoneNumber: String,
    val email: String
) : Parcelable

@Parcelize
data class CustomerContent(val content: List<CustomerResponse>) : Parcelable