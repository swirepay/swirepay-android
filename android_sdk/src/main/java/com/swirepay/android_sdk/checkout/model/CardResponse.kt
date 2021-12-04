package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Country
import com.swirepay.android_sdk.model.Currency
import com.swirepay.android_sdk.model.Customer
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val customer: CustomerResponse,
    val name: String,
    val scheme: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val lastFour: String,
    val fingerprint: String,
    val type: String,
    val bankName: String,
    val country: Country,
    val currency: Currency,
    val isPresent: Boolean,
    val brand: String,
    val expiresAt: String,
    val device: Boolean,
    val deleted: Boolean,
) : Parcelable