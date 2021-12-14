package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentMethodCard(
    val card: Card,
    val type: String,
    val customerGid: String,
    val saved: Boolean
) : Parcelable

@Parcelize
data class Card(
    val cvv: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val name: String,
    val number: String
) : Parcelable

