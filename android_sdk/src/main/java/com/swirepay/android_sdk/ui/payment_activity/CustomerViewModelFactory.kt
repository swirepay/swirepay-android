package com.swirepay.android_sdk.ui.payment_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk

class CustomCustomerDetailsViewModelProvider(val amount: Int, val currencyType: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelPayment(amount , currencyType) as T
    }

}