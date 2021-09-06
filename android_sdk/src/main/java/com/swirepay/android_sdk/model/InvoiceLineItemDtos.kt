package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InvoiceLineItemDtos (
        val amount: String,
        val name: String,
        val description: String,
        val quantity: String,
        val note: String,
        val rate: String,
        val currencyCode: CurrencyType
    ) : Parcelable