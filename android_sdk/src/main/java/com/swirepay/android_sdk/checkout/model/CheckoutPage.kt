package com.swirepay.android_sdk.checkout.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class CheckoutPage(
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
    val paymentSession: String
) : Parcelable

@Parcelize
class Currency(
    val id: Int,
    val name: String,
    val prefix: String,
    val toFixed: Int,
    val countryAlpha2: String
) : Parcelable