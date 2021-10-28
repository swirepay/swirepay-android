package com.swirepay.android_sdk.model.pusher

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentRequest(
    val type: String,
    val paymentRequest: Payment,
) : Parcelable

@Parcelize
class Payment(
    val uuid: String?,
    val serviceNumber: String?,
    val name: String,
    val amount: Int,
    val description: String?
) : Parcelable
