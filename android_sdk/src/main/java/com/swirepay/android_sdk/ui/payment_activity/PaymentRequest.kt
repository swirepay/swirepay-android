package com.swirepay.android_sdk.ui.payment_activity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentRequest(val amount : String , val currencyCode : String , val paymentMethodType : List<String>) : Parcelable