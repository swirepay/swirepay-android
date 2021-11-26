package com.swirepay.android_sdk.model

import android.os.Parcelable
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import kotlinx.parcelize.Parcelize

@Parcelize
class PaymentRequest(
    val amount: String,
    val currencyCode: String,
    val paymentMethodType: List<String>,
    val customer: CustomerModel?,
    val customerGid: String?,
    val notificationType: String,
    val dueDate: String?,
) : Parcelable
