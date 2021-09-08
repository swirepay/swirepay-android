package com.swirepay.android_sdk.ui.subscription_button.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Currency
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SubscriptionButton(
    val gid: String,
    val currency: Currency,
    val plan: Plan?,
    val status: String,
    val link: String,
    val account: Account,
    val coupon: Coupon?,
    val taxAmount: List<TaxInfo>?
) : Parcelable

@Parcelize
data class Account(
    val gid: String,
    val name: String,
    val timezone: String,
    val largeLogo: String
) : Parcelable

@Parcelize
data class TaxRate(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val displayName: String,
    val description: String, val jurisdiction: String,
    val percentage: Float, val inclusive: Boolean
) : Parcelable

@Parcelize
data class TaxInfo(
    val taxRates: TaxRate,
    val tax: Int
) : Parcelable

@Parcelize
data class Coupon(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val validity: String,
    val amountOff: Int,
    val active: Boolean,
    val name: String,
    val currency: Currency
) : Parcelable