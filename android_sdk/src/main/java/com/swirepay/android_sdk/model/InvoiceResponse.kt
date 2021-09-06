package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceResponse(
    val gid: String,
    val link: String,
    val invoiceLinks:ArrayList<InvoiceLinks>
) : Parcelable
