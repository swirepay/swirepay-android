package com.swirepay.android_sdk.ui.payment_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.NotificationType

class CustomCustomerDetailsViewModelProvider(val amount: Int, val currencyType: String , val paymentMethods : List<String> , val email : String ,
                                             val phoneNumber : String , val notificationType: String , val name : String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelPayment(amount , currencyType , paymentMethods , email = email , phoneNumber =  phoneNumber , notificationType = notificationType , paymentName = name) as T
    }

}