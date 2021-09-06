package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceResponse(
    val gid: String?,
    val account: InvoiceAccount,
    val invoice: Invoice,
    val invoiceLineItems: List<InvoiceLineItems>
) : Parcelable

@Parcelize
data class InvoiceAccount(
    val gid: String?,
    val name: String?,
    val support: String?,
    val largeLogo: String?,
    val timezone: String?,
    val country: Country,
    val currency: Currency,
    val supportedPaymentTypes: List<String>,
    val supportedRecurringPaymentTypes: List<String>
) : Parcelable

@Parcelize
data class Country(
    val id: Int,
    val name: String?,
    val alpha2: String?,
    val alpha3: String?,
    val unCode: String?
) : Parcelable

@Parcelize
data class Invoice(
    val gid: String?,
    val customer: Customer,
    val paymentMethod: PaymentMethod,
    val billingAddress: String?,
    val shippingAddress: String?,
    val invoiceLineItems: List<InvoiceLineItems>,
    val taxAmount: String?,
    val total: Int,
    val discountAmount: Int,
    val shippingAmount: String?,
    val paymentMethodType: List<String>,
    val description: String?,
    val amount: Int,
    val statementDescriptor: String?,
    val paymentDueDate: String?,
    val paymentDate: String?,
    val status: String?,
    val lastProcessed: String?,
    val issueDate: String?,
    val customerNote: String?,
    val invoiceNumber: String?,
    val invoicePdfKey: String?,
    val currency: Currency,
    val coupon: Coupon?,
) : Parcelable

@Parcelize
data class Customer(
    val gid: String?,
    val name: String?,
    val email: String?,
    val phoneNumber: String?,
    val billingAddress: String?,
    val shippingAddress: String?,
    val iavToken: String?,
) : Parcelable

@Parcelize
data class PaymentMethod(
    val gid: String?,
    val card: Card,
    val bank: String?,
    val default: Boolean
) : Parcelable

@Parcelize
data class Card(
    val name: String?,
    val lastFour: String?,
    val scheme: String?,
    val expiryMonth: Int,
    val expiryYear: String?
) : Parcelable

@Parcelize
data class Coupon(
    val gid: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val validity: String?,
    val amountOff: Int,
    val name: String?,
    val maximumRedemption: String?,
    val currency: Currency,
    val promotionalCodes: String?,
    val couponRedemption: String?,
    val redemptionCount: Int,
    val active: Boolean,
    val valid: String?,
    val deleted: Boolean
) : Parcelable
