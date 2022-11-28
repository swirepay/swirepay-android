package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderInfo(
    var receiptEmail: String? = null,
    var receiptSms: String? = null,
    var currencyCode: String? = null,
    var description: String? = null,
    var statementDescriptor: String? = null,
    var paymentMethodType: List<PaymentMethodType> = emptyList(),
    var confirmMethod: String? = "AUTOMATIC",
    var captureMethod: String? = "AUTOMATIC",
    var amount: Int = 0,
    var paymentMethodGid: String? = null
) : Parcelable 