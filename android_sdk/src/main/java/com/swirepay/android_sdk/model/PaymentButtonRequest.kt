package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentButtonRequest(val amount : Int ,val  currencyCode : String ,val  description : String ,
    val notificationType: String , val paymentMethodType : List<String>
                           ) : Parcelable