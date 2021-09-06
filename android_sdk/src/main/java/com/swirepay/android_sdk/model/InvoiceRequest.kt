package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InvoiceRequest(
    val currencyCode: String,
    val customerGid: String,
    val couponGid: String,
    val daysUntilDue: String,
    val paymentDueDate: String,
    val issueDate: String,
    val amount: Int,
    val status: String,
    val description: String,
    val taxRates: ArrayList<String>,
    val customerNote: String,
    val billingAddress: String?,
    val shippingAddress: String?,
    val subscriptionGid: String?,
    val invoiceNumber: String?,
    val invoiceLineItems: ArrayList<InvoiceLineItems>,
    val invoiceLineItemDtos: ArrayList<InvoiceLineItemDtos>,
) : Parcelable