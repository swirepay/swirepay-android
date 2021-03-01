package com.swirepay.android_sdk.ui.subscription_button

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CustomSubscriptionButtonViewModelProvider(val name : String , val amount : Int ,
                                                val description : String , val currencyCode : String , val billingFrequency : String,
                                                val billingPeriod : Int , val planStartDate : String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelSubscriptionButton(name, amount , description , currencyCode , billingFrequency , billingPeriod , planStartDate) as T
    }

}
