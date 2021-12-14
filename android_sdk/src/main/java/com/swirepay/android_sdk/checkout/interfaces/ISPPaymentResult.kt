package com.swirepay.android_sdk.checkout.interfaces


import com.swirepay.android_sdk.checkout.model.PaymentCustomer
import com.swirepay.android_sdk.checkout.model.PaymentMethodResponse
import com.swirepay.android_sdk.model.Currency

interface ISPPaymentResult {

    val gid: String
    val paymentMethod: PaymentMethodResponse
    val currency: Currency
    val customer: PaymentCustomer
    val status: String
    val amount: Int
}