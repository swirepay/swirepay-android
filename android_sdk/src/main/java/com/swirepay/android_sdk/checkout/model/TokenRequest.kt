package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenRequest(
    val type: String,
    val card: TokenCard
) : Parcelable

@Parcelize
data class TokenCard(
    val cvv: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val name: String,
    val number: String,
    val scheme: String
) : Parcelable
