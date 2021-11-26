package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentSession(
    val receiptEmail: String,
    val receiptSms: String,
    val currencyCode: String,
    val description: String,
    val statementDescriptor: String,
    val paymentMethodType: ArrayList<String>,
    val confirmMethod: String,
    val captureMethod: String,
    val amount: Int,
    val paymentMethodGid: String,
) : Parcelable