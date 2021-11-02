package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceLineItems(
    val amount: Int,
    val name: String?,
    val quantity: Int,
) : Parcelable