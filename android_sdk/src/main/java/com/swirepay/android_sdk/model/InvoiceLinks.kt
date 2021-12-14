package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class InvoiceLinks(
    val gid: String,
    val link: String,
    val invoiceGid: String,
    val redirectUri: String,
    val nextActionUrl: String,
    val paymentSession: String,
    val notificationType: String,
    val deleted: Boolean
) : Parcelable