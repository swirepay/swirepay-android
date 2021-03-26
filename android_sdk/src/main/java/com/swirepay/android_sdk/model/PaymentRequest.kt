package com.swirepay.android_sdk.model

import android.os.Parcelable
import com.swirepay.android_sdk.Utility
import kotlinx.parcelize.Parcelize

@Parcelize
class PaymentRequest(val amount : String , val currencyCode : String , val paymentMethodType : List<String> , val redirectUri : String = Utility.baseUrl) : Parcelable