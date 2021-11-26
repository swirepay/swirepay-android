package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CheckoutPaymentResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val amount: Int,
    val paymentMethodType: ArrayList<String>,
    val redirectUri: String,
    val expiresAt: String,
    val status: String,
    val link: String,
    val currency: Currency,
    val paymentSession: PaymentSession,
) : Parcelable

