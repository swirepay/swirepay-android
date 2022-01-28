package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val livePortal: Portal,
    val testPortal: Portal,
    val name: String,
    val contactNumber: String,
    val contactEmail: String,
    val status: String,
    val contactGivenName: String,
    val contactFamilyName: String,
    val timezone: String,
    val shouldNotify: Boolean,
    val supportedPaymentTypes: List<String>,
    val supportedOfflinePaymentTypes: List<String>,
    val supportedCurrencies: List<String>,
    val supportedRecurringPaymentTypes: List<String>,
    val swirepayUsPoolId: String,
    val dwollaCustomerStatus: String,
    val iciciCustomerStatus: String,
    val active: Boolean,
    val sandbox: String,
    val issuerActive: Boolean,
    val deleted: Boolean,
    val currency: Currency
) : Parcelable

@Parcelize
data class Portal(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val test: Boolean,
    val deleted: Boolean
) : Parcelable

