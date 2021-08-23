package com.swirepay.android_sdk.ui.subscription_button

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.model.PaymentButtonRequest
import com.swirepay.android_sdk.ui.payment_button.ViewModelPaymentButton

class CustomSubscriptionButtonViewModelProvider(
    val name: String,
    val planAmount: Int,
    val description: String,
    val currencyCode: String,
    val planBillingFrequency: String,
    val planBillingPeriod: Int,
    val planStartDate: String,
    val taxRates: List<String>?,
    val couponId: String?,
    val planQuantity: Int,
    val planTotalPayments: Int,
    val status: String?
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelSubscriptionButton(
            name,
            planAmount,
            description,
            currencyCode,
            planBillingFrequency,
            planBillingPeriod,
            planStartDate,
            taxRates,
            couponId,
            planQuantity,
            planTotalPayments,
            status,
        ) as T
    }

}


class CustomPaymentButtonViewModelProvider(
    val paymentButtonRequest: PaymentButtonRequest
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelPaymentButton(paymentButtonRequest) as T
    }

}
