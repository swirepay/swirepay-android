package com.swirepay.android_sdk.model

import android.os.Parcelable
import com.swirepay.android_sdk.Utility
import kotlinx.parcelize.Parcelize

@Parcelize
class PaymentRequest(val amount : String , val currencyCode : String , val paymentMethodType : List<String> , val redirectUri : String = Utility.baseUrl ,
                     val notificationType : String , val email : String , val phoneNumber : String , val name : String) : Parcelable