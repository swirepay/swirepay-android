package com.swirepay.android_sdk.ui.subscription_button

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.model.PaymentButtonRequest
import com.swirepay.android_sdk.ui.payment_button.ViewModelPaymentButton

class CustomSubscriptionButtonViewModelProvider(
    val name: String, val amount: Int,
    val description: String, val currencyCode: String, val billingFrequency: String,
    val billingPeriod: Int, val planStartDate: String , val listOfTaxIds : List<String>? , val couponId : String? , val totalCount : Int , val planQuantity : Int
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelSubscriptionButton(
            name,
            amount,
            description,
            currencyCode,
            billingFrequency,
            billingPeriod,
            planStartDate, listOfTaxIds , couponId  , planQuantity  ,totalCount
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
