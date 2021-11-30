package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import com.swirepay.android_sdk.checkout.interfaces.ISPPaymentResult
import com.swirepay.android_sdk.model.Country
import com.swirepay.android_sdk.model.PaymentMethod
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentSessionResponse(
    override val gid: String,
    val createdAt: String,
    val updatedAt: String,
    override val paymentMethod: PaymentMethodResponse,
    override val currency: Currency,
    override val customer: PaymentCustomer,
    override val status: String,
    override val amount: Int,
    val amountRefunded: Int,
    val description: String,
    val statementDescriptor: String,
    val paymentMethodType: ArrayList<String>,
    val meta: String,
    val receiptEmail: String,
    val receiptSms: String,
    val confirmMethod: String,
    val captureMethod: String,
    val errorCode: String,
    val errorDescription: String,
    val amountUnCaptured: Int,
    val amountReceived: Int,
    val refundReason: String,
    val disputeStatus: String,
    val disputeType: String,
    val respondBy: String,
    val refundNote: String,
    val paymentDate: String,
    val refundDate: String,
    val disputeDate: String,
    val amountDisputed: String,
    val disputeFee: String,
    val receiptNumber: String,
    val feeAmount: Int,
    val feeTax: Int,
    val net: Int,
    val mdr: String,
    val nextActionUrl: String,
    val psClientSecret: String,
    val returnUrl: String,
    val secureStatus: String,
    val paymentLinkId: String,
    val paymentLinkGid: String,
    val checkoutPageGid: String,
    val checkoutPageId: String,
    val subscription: String,
    val invoice: String,
    val paymentButtonId: String,
    val paymentButtonGid: String,
    val paymentPage: String,
    val mrn: String,
    val authorizationId: String,
    val authCode: String,
    val partnerAccountGid: String,
    val applicationFee: Int,
    val paymentWidgetId: String,
    val paymentWidgetGid: String,
    val terminal: String,
    val batchNumber: String,
    val internalRefund: String,
    val deleted: Boolean
) : Parcelable,ISPPaymentResult

@Parcelize
data class PaymentCustomer(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val tax: Tax,
    val taxStatus: String,
    val billingAddress: _BillingAddress,
    val shippingAddress: _ShippingAddress,
    val referenceNumber: String,
    val doNotDuplicate: Boolean,
    val customerProfileDto: String,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class Tax(
    val id: Int,
    val type: String,
    val country: String
) : Parcelable

@Parcelize
data class _BillingAddress(
    val id: Int,
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: Country,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class _ShippingAddress(
    val id: Int,
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: Country,
    val deleted: Boolean
) : Parcelable