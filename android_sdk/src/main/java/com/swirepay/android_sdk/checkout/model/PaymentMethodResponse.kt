package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Country
import com.swirepay.android_sdk.model.Currency
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentMethodResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val paymentType: PaymentMethodType,
    val card: PaymentCard,
    val isVerified: Boolean,
    val referenceId: String,
    val upi: PaymentUpi,
    val bank: String,
    val netbanking: PaymentNetBanking,
    val default: Boolean,
    val device: Boolean,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class PaymentUpi(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val vpa: String,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class PaymentNetBanking(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val bankId: Int,
    val bankName: String,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class PaymentMethodType(
    val id: Int,
    val name: String,
    val category: String,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean,
    val recurringPaymentType: Boolean
) : Parcelable

@Parcelize
data class PaymentCard(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val scheme: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val lastFour: String,
    val fingerprint: String,
    val type: String,
    val bankName: String,
    val country: Country,
    val currency: Currency,
    val isPresent: Boolean,
    val brand: String,
    val expiresAt: String,
    val device: Boolean,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class PaymentMethodContent(val content: List<PaymentMethodResponse>) : Parcelable

@Parcelize
data class _PaymentMethodCard(val paymentCard: PaymentCard, val gid: String) : Parcelable