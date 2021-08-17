package com.swirepay.android_sdk.ui.payment_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel

class CustomCustomerDetailsViewModelProvider(
    val amount: Int,
    val currencyType: String,
    val paymentMethods: List<String>,
    val customer: CustomerModel,
    val notificationType: String,
    val customerGid: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelPayment(
            amount,
            currencyType,
            paymentMethods,
            customer,
            notificationType = notificationType,
            customerGid
        ) as T
    }

}