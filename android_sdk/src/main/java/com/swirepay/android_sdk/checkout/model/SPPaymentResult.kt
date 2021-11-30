package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import com.swirepay.android_sdk.checkout.interfaces.ISPPaymentResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class SPPaymentResult(
    val paymentSession: _PaymentSession,
    val paymentMethod: _PaymentMethod,
    var card: _PaymentCard?,
    val upi: _PaymentUpi?,
    val netbanking: _PaymentNetBanking?,
    val customer: _Customer
) : Parcelable

@Parcelize
data class PaymentResult(
    override val paymentMethod: PaymentMethodResponse,
    override val gid: String,
    override val currency: Currency,
    override val status: String,
    override val amount: Int,
    override val customer: PaymentCustomer
) : Parcelable, ISPPaymentResult

@Parcelize
data class _PaymentSession(
    val gid: String,
    val amount: Int,
    val currency: String,
    val authCode: String,
    val paymentDate: String,
    val meta: String?,
    val status: String
) : Parcelable

@Parcelize
data class _PaymentMethod(
    val gid: String,
    val paymentType: _PaymentType
) : Parcelable

@Parcelize
data class _PaymentCard(
    val gid: String,
    val scheme: String,
    val expYear: Int,
    val expMonth: Int,
    val lastFour: String
) : Parcelable

@Parcelize
data class _PaymentType(
    val category: String
) : Parcelable

@Parcelize
data class _PaymentUpi(
    val gid: String,
    val vpa: String
) : Parcelable

@Parcelize
data class _PaymentNetBanking(
    val gid: String,
    val bankName: String
) : Parcelable

@Parcelize
data class _Customer(
    val gid: String,
    val name: String,
    val email: String,
    val phone: String
) : Parcelable