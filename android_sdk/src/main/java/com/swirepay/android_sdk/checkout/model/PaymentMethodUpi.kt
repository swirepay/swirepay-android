package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentMethodUpi(val upi: Upi, val type: String, val customerGid: String) : Parcelable

@Parcelize
data class Upi(val vpa: String) : Parcelable

@Parcelize
data class SPCustomer(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var billingAddress: SPBillingAddress,
    var shippingAddress: SPShippingAddress
) : Parcelable

@Parcelize
data class SPBillingAddress(
    var street: String,
    var city: String,
    var state: String,
    var postalCode: String,
    var countryCode: String
) : Parcelable

@Parcelize
data class SPShippingAddress(
    var street: String,
    var city: String,
    var state: String,
    var postalCode: String,
    var countryCode: String
) : Parcelable

